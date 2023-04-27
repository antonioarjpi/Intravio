package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.service.exceptions.RuleOfBusinessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
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

    private String extrairExtensao(String nomeArquivo) {
        int i = nomeArquivo.lastIndexOf(".");
        return nomeArquivo.substring(i + 1);
    }
}
