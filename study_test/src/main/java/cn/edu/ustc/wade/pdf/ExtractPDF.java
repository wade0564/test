package cn.edu.ustc.wade.pdf;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.common.io.Files;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class ExtractPDF {

 

         /** The resulting text file with info about a PDF. */

    public static final String RESULT  = "d:/test.txt";//存放由pdf转换成txt文件的路径。

    /**

     * Main method.

     * @param args no arguments needed

     * @throws DocumentException

     * @throws IOException

     */

    public static void main(String[] args)

        throws DocumentException, IOException {

        PrintWriter writer = new PrintWriter(new FileOutputStream(RESULT));//txt文件写入流

        String string = "D:/test.pdf";//pdf文件路径
        
//        Files.
        
//        inspect(writer,string); //调用读取方法

//        writer.close();

    }

    /**

     * Inspect a PDF file and write the info to a txt file

     * @param writer Writer to a text file

     * @param filename Path to the PDF file

     * @throws IOException

     */

    public static void inspect(PrintWriter writer, String filename)

        throws IOException {

        PdfReader reader = new PdfReader(filename); //读取pdf所使用的输出流

        int num = reader.getNumberOfPages();//获得页数

        String content = "";  //存放读取出的文档内容

        for (int i = 1; i < num; i++) {

           content += PdfTextExtractor.getTextFromPage(reader, i); //读取第i页的文档内容

                  }

       writer.write(content);//写入文件内容

        writer.flush();

    }

}