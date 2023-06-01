package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.service.exceptions.FileNotFoundException;
import com.intraviologistica.intravio.service.exceptions.RuleOfBusinessException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipOutputStream;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileServiceTest {

    @Mock
    private File file;

    @InjectMocks
    private FileService fileService;

    @Mock
    private ZipOutputStream zipOut;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEnviaArquivo_QuandoSucesso() throws Exception {
        byte[] content = "Conteúdo do arquivo".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.png", "text/plain", content);

        // Execute o método que deseja testar
        String path = "src/main/resources/static/pedidos/teste/";
        String filename = fileService.enviaArquivo(multipartFile, path);

        // Verifique o resultado
        assertNotNull(filename);
        assertTrue(filename.endsWith(".png"));

        fileService.excluiFotoDoPedido(new MultipartFile[0], List.of(filename), path);
    }

    @Test
    public void testEnviaArquivoComExtensoesNaoPermitidas() {
        byte[] content = "Conteúdo do arquivo".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.pdf", "application/pdf", content);

        // Execute o método que deseja testar e verifique se a exceção é lançada
        assertThrows(RuleOfBusinessException.class, () ->
                fileService.enviaArquivo(multipartFile, "src/main/resources/static/pedidos/teste/"));
    }

    @Test
    public void testExcluiFotoDoPedido() throws IOException {
        Path tempDirectory = Files.createTempDirectory("test-directory"); // Diretório temporário para os testes

        String path = "src/main/resources/static/pedidos/teste/";
        List<String> imagens = Arrays.asList("foto1.jpg", "foto2.jpg", "foto3.jpg");

        // Criação dos arquivos no diretório temporário
        File foto1 = new File(path + File.separator + "foto1.jpg");
        File foto2 = new File(path + File.separator + "foto2.jpg");
        File foto3 = new File(path + File.separator + "foto3.jpg");

        assertTrue(foto1.createNewFile());
        assertTrue(foto2.createNewFile());
        assertTrue(foto3.createNewFile());

        FileService fileService = new FileService();
        fileService.excluiFotoDoPedido(new MultipartFile[0], imagens, path);

        // Verificação se os arquivos corretos foram excluídos
        assertFalse(foto1.exists());
        assertFalse(foto2.exists());
        assertFalse(foto3.exists());

        // Remoção do diretório temporário após o teste
        Files.delete(tempDirectory);
    }

    @Test
    public void testBaixarArquivos() throws Exception {
        String path = "src/main/resources/static/pedidos/teste/";

        byte[] content = "Conteúdo do arquivo".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.png", "text/plain", content);

        String filename = fileService.enviaArquivo(multipartFile, path); // Execute o método que deseja testar

        List<String> filenames = Arrays.asList(filename);
        Path path1 = Files.createTempFile(filename, ".png");
        Files.write(path1, content);

        when(response.getOutputStream()).thenReturn(new MockServletOutputStream()); // Defina o comportamento esperado para o objeto HttpServletResponse mock

        fileService.baixarArquivos(filenames, path, response); // Execute o método que deseja testar

        // Verifique se os métodos apropriados foram chamados corretamente
        verify(response).setContentType("application/zip");
        verify(response).setHeader("Content-Disposition", "attachment; filename=\"imagens.zip\"");

        fileService.excluiFotoDoPedido(new MultipartFile[0], List.of(filename), path);
    }

    @Test
    public void testBaixarArquivos_QuandoNaoEncontrarArquivos() throws IOException {
        when(response.getOutputStream()).thenReturn(new MockServletOutputStream());

        String path = "src/main/resources/static/pedidos/teste/";

        FileService fileService = new FileService();

        List<String> filenames = Arrays.asList("foto1.jpg", "foto2.jpg", "foto3.jpg");

        catchThrowableOfType(() -> fileService.baixarArquivos(filenames, path, response), FileNotFoundException.class);
    }

    @Test
    public void testBaixarArquivo() throws Exception {
        String paths = "src/main/resources/static/pedidos/teste/";
        String filename = "imagem.png";
        byte[] content = "Conteúdo do arquivo".getBytes();
        Path path = Files.createTempFile("imagem", ".png");
        Files.write(path, content);

        // Defina o comportamento esperado para o objeto HttpServletResponse mock
        when(response.getOutputStream()).thenReturn(new MockServletOutputStream());

        assertDoesNotThrow(() -> fileService.baixarArquivo(filename, paths, response));

        // Verifique se os métodos apropriados foram chamados corretamente
        verify(response).setHeader("Content-Disposition", "attachment;filename=" + filename);
        verify(response).setContentType("application/octet-stream");
    }


    private static class MockServletOutputStream extends ServletOutputStream {
        @Override
        public void write(int b) {
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }
}