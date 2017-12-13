/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.textconwebservice;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 *
 * @author Mauricio
 */
@WebService(serviceName = "ConversorService")
public class ConversorService {

    /**
     *
     * @param bytes
     * @param nombre
     * @return
     */
    @WebMethod(operationName = "convertidorGlobal")
    public String convertidorGlobal(@WebParam(name = "bytes") byte[] bytes, @WebParam(name = "nombre") String nombre) {

        String tipo = nombre.substring(nombre.lastIndexOf(".") + 1, nombre.length()).toLowerCase();
        String extraido = "";

        switch (tipo) {
            case "pdf":
                extraido = convertirPDF(bytes);
                break;
            case "doc":
                extraido = convertirDOC(bytes);
                break;
            case "docx":
                extraido = convertirDOCX(bytes);
                break;
        }
        return extraido;
    }

    /**
     *
     * @param bytes
     * @return
     */
    @WebMethod(operationName = "convertirPDF")
    public String convertirPDF(@WebParam(name = "bytes") byte[] bytes) {
        String texto = "";
        try {
            //Crea un archivo temporal donde traducir los bytes
            File archivo = new File("temp.pdf");
            FileOutputStream fos = new FileOutputStream(archivo.getAbsolutePath());
            fos.write(bytes);
            fos.close();
            PdfReader lector = new PdfReader(archivo.getAbsolutePath());
            for (int i = 1; i <= lector.getNumberOfPages(); i++) {
                texto += PdfTextExtractor.getTextFromPage(lector, i);
            }
            archivo.delete();
        } catch (IOException ex) {
            Logger.getLogger(ConversorService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return texto;
    }

    /**
     *
     * @param bytes
     * @return
     */
    @WebMethod(operationName = "convertirDOCX")
    public String convertirDOCX(@WebParam(name = "bytes") byte[] bytes) {
        String texto = "";
        try {
            //Crea un archivo temporal donde traducir los bytes
            File archivo = new File("temp.docx");
            FileOutputStream fos = new FileOutputStream(archivo.getAbsolutePath());
            fos.write(bytes);
            fos.close();

            XWPFDocument docx = new XWPFDocument(new FileInputStream("temp.docx"));

            XWPFWordExtractor extraido = new XWPFWordExtractor(docx);
            archivo.delete();
            texto = extraido.getText();

        } catch (IOException ex) {
            Logger.getLogger(ConversorService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return texto;
    }

    /**
     *
     * @param bytes
     * @return
     */
    @WebMethod(operationName = "convertirDOC")
    public String convertirDOC(@WebParam(name = "bytes") byte[] bytes) {
        String texto = "";

        try {
            //Crea un archivo temporal donde traducir los bytes
            File archivo = new File("temp.doc");
            FileOutputStream fos = new FileOutputStream(archivo.getAbsolutePath());
            fos.write(bytes);
            fos.close();
            WordExtractor doc = new WordExtractor(new FileInputStream("temp.doc"));
            archivo.delete();
            texto = doc.getText();

        } catch (IOException ex) {
            Logger.getLogger(ConversorService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return texto;
    }

    /**
     *
     * @param bytes
     * @return
     */
    @WebMethod(operationName = "convertirTXT")
    public String convertirTXT(@WebParam(name = "bytes") byte[] bytes) {
        String texto = "";

        try {
            //Crea un archivo temporal donde traducir los bytes
            File archivo = new File("temp.txt");
            FileOutputStream fos = new FileOutputStream(archivo.getAbsolutePath());
            fos.write(bytes);
            fos.close();

            String aux;
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            while ((aux = b.readLine()) != null) {
                texto += aux + "\n";
            }

            archivo.delete();

        } catch (IOException ex) {
            Logger.getLogger(ConversorService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return texto;
    }
}
