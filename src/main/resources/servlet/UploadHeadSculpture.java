import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

@WebServlet(name = "UploadHeadSculpture",value = "/UploadHeadSculpture")
public class UploadHeadSculpture extends HttpServlet {
    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";

    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 800; // 800MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 1000; // 1000MB

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") != null) {
            if (!ServletFileUpload.isMultipartContent(request)) {
                PrintWriter writer = response.getWriter();
                writer.print("Error: 表单必须包含 enctype=multipart/form-data");
                writer.flush();
                return;
            }

            //配置上传参数
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置内存临界值-超过后将产生临时文件并存储于临时目录中
            factory.setSizeThreshold(MEMORY_THRESHOLD);
            // 设置临时存储目录
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

            ServletFileUpload upload = new ServletFileUpload(factory);
            //设置文件上传最大上传值
            upload.setFileSizeMax(MAX_FILE_SIZE);
            // 设置最大请求值 (包含文件和表单数据)
            upload.setSizeMax(MAX_REQUEST_SIZE);
            // 中文处理
            upload.setHeaderEncoding("UTF-8");
            // 构造临时路径来存储上传的文件
            // 这个路径相对当前应用的目录
            String HeadSculpturePath = "E:\\Redrock\\Redrock_web\\WorkWinter\\src\\main\\webapp\\HeadSculpture";

            File HeadSculptureDir = new File(HeadSculpturePath);
            if (!HeadSculptureDir.exists()) {
                HeadSculptureDir.mkdir();
            }

            try {
                List<FileItem> fileItems = upload.parseRequest(request);
                if (fileItems != null && fileItems.size() > 0) {

                    for (FileItem item : fileItems) {

                        if (!item.isFormField()) {
                            String imageRegex = "image/[a-z|A-Z]*";
                            String type = item.getContentType();
                            if (type.matches(imageRegex)) {
                                System.out.println("上传的是图片");
                                //得到数据库链接

                                String filename = new File(item.getName()).getName();
                                String filepath = HeadSculpturePath + File.separator + filename;
                                String headurl = "http://menghong.x2.natapp.link//HeadSculpture//" + filename;
                                Connection con = new DBConnction().getConnction();
                                String sql = "UPDATE users SET headSculpture = ? WHERE username = \'"+session.getAttribute("username")+"\'";
                                PreparedStatement pst = con.prepareStatement(sql);
                                pst.setString(1, headurl);
                                int result = pst.executeUpdate();
                                if (result == 1) {
                                    System.out.println("插入数据库成功！");
                                    File storeFile = new File(filepath);
                                    item.write(storeFile);
                                }
                                System.out.println("头像上传成功");
                            }
                        }
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
