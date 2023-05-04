package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.service.exceptions.FileNotFoundException;
import com.intraviologistica.intravio.service.exceptions.RuleOfBusinessException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    public String enviaArquivo(MultipartFile file, String path) throws Exception {
        String extensao = extrairExtensao(file.getOriginalFilename());

        List<String> extensoesPermitidas = Arrays.asList("png", "jpg", "jpeg", "heic", "heif", "bmp", "tiff");

        if (!extensoesPermitidas.contains(extensao)) {
            throw new RuleOfBusinessException("Somente arquivos de imagem são permitidos");
        }

        String filename = UUID.randomUUID() + "." + extrairExtensao(file.getOriginalFilename());
        String caminho = path + filename;

        try {
            Files.copy(file.getInputStream(), Path.of(caminho), StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Procura e remove os fotos do pedido
    public void excluiFotoDoPedido(MultipartFile[] files, List<String> imagens, String path) {
        if (imagens != null && !imagens.isEmpty()) {
            File folder = new File(path);
            File[] filesExclude = folder.listFiles();
            if (files != null) {
                for (File file : filesExclude) {
                    if (imagens.contains(file.getName())) {
                        file.delete();
                    }
                }
            }
        }
    }

    @Transactional
    public void baixarArquivo(String filename, String path, HttpServletResponse response) throws FileNotFoundException {
        Path arquivo = Paths.get(path + filename);
        if (Files.exists(arquivo)) {
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setContentType("application/octet-stream");
        } else {
            throw new FileNotFoundException("Nenhum arquivo encontrado.");
        }
        try {
            Files.copy(arquivo, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private String extrairExtensao(String nomeArquivo) {
        int i = nomeArquivo.lastIndexOf(".");
        return nomeArquivo.substring(i + 1);
    }
}
