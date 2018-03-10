import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet (name = "UploadServlet", value = "/UploadServlet")
public class UploadServlet extends HttpServlet{
    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";

    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 800; // 800MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 1000; // 1000MB

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            PrintWriter writer = response.getWriter();
            writer.print("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return;
        }
        //得到数据库链接
        Connection con = new DBConnction().getConnction();
        System.out.println("1");
        //得到浏览器传来的参数
        String title=request.getParameter("title");
        System.out.println(request.getParameter("title"));
        System.out.println(request.getParameter("Did"));
        int Did=Integer.parseInt(request.getParameter("Did"));
        //String coverURL=request.getParameter("coverURl");
        String videoURL="http://menghong.x2.natapp.link/video/";
        System.out.println("2");


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
        String videoUploadPath = "E:\\Redrock\\Redrock_web\\WorkWinter\\src\\main\\webapp\\video\\";
        // 如果目录不存在则创建
        File videoUploadDir = new File(videoUploadPath);
        if (!videoUploadDir.exists()) {
            videoUploadDir.mkdir();
        }
        String imageUploadPath = "E:\\Redrock\\Redrock_web\\WorkWinter\\src\\main\\webapp\\videoCover\\";
        // 如果目录不存在则创建
        File imageUploadDir = new File(imageUploadPath);
        if (!imageUploadDir.exists()) {
            imageUploadDir.mkdir();
        }
        // 解析请求的内容提取文件数据
        try {
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                //迭代表单数据
                for (FileItem item : formItems) {
                    //处理不在表单中的字段
                    if (!item.isFormField()) {
                        System.out.println("3");
                        //正则表达式判断是video还是image
                        String videoRegex = "video/[a-z|A-Z]*";
                        String imageRegex="image/[a-z|A-Z]*";
                        String type = item.getContentType();
                        System.out.println(type);
                        if (type.matches(videoRegex)) {
                            System.out.println("上传的是video");
                            String fileName = new File(item.getName()).getName();
                            String filepath = videoUploadPath + fileName;
                            File storeFile = new File(filepath);
                            //控制台输出文件的上传路径
                            System.out.println(filepath);
                            //保存到硬盘
                            item.write(storeFile);
                            //插入数据库语句
                        String sql="INSERT INTO videos (title,videoURL,id,Did) VALUES (?,?,?,?)";
                        PreparedStatement pst=con.prepareStatement(sql);
                        videoURL+=fileName;
                        pst.setString(1,title);
                        pst.setString(2,videoURL);
                        pst.setInt(4,Did);

                        int b=pst.executeUpdate();
                        if (b == 1) {
                            request.setAttribute("message", "文件上传成功!");
                            System.out.println("上传成功");
                        }
                        else {
                            request.setAttribute("message", "文件上传失败!");
                        }
                        }else if (type.matches(imageRegex)){
                            System.out.println("上传的是image");
                            String fileName = new File(item.getName()).getName();
                            String filepath = imageUploadPath + fileName;
                            File storeFile = new File(filepath);
                            //控制台输出文件的上传路径
                            System.out.println(filepath);
                            //保存到硬盘
                            item.write(storeFile);
                            //插入数据库语句（还没写）
                            String coverURL="http://menghong.x2.natapp.link/videoCover/"+fileName;
                            String sql="UPDATE users SET coverURL = ?  WHERE title = \'"+title+"\'";
                            PreparedStatement pst=con.prepareStatement(sql);
                            pst.setString(1,coverURL);
                            int result=pst.executeUpdate();
                            if (result==1){
                                System.out.println("封面上传数据库成功");
                            }else {
                                System.out.println("封面上传数据库失败！");
                            }
                        }
                    }

                    }
                }
            } catch(FileUploadException e){
                e.printStackTrace();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        }


