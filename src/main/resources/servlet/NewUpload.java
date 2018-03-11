
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "NewUpload" , value = "/NewUpload")
public class NewUpload extends HttpServlet {
    //得到的title参数
    String title = "";
    //得到的Did参数
    int Did=0;
    String videoURL="";
    String videoCover="";
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
        request.setCharacterEncoding("UTF-8");
        if (!ServletFileUpload.isMultipartContent(request)) {
            PrintWriter writer = response.getWriter();
            writer.print("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return;
        }

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("utf-8");


        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 中文处理
        upload.setHeaderEncoding("UTF-8");

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录

        //视频上传地址
        String videoUploadPath = "E:\\Redrock\\Redrock_web\\WorkWinter\\src\\main\\webapp\\video\\";
        // 如果目录不存在则创建
        File videoUploadDir = new File(videoUploadPath);
        if (!videoUploadDir.exists()) {
            videoUploadDir.mkdir();
        }
        //封面上传地址
        String imageUploadPath = "E:\\Redrock\\Redrock_web\\WorkWinter\\src\\main\\webapp\\videoCover\\";
        // 如果目录不存在则创建
        File imageUploadDir = new File(imageUploadPath);
        if (!imageUploadDir.exists()) {
            imageUploadDir.mkdir();
        }

        //解析请求的内容

        try {
            List<FileItem> fileItems=upload.parseRequest(request);
            if (fileItems!=null&&fileItems.size()>0){
                for (FileItem item:fileItems) {
                    //处理在表单中的数据
                    if (item.isFormField()){
                        //是表单组件 就得到了 <input type="text" name="content"> 这样的组件
                        String fieldName=item.getFieldName();
                        System.out.println(fieldName);
                        //得到上传文件的名字
                        String name =item.getName();
                        System.out.println(name);
                        //
                        String string=item.getString("utf-8");
                        System.out.println(string);

                        if (fieldName.equals("title")){
                            title=string;
                            System.out.println("title:"+title);
                        }
                        else {
                            Did=Integer.parseInt(string);
                            System.out.println("Did:"+Did);
                        }
                    }
                    else {
                        // 不是表单组件 这就得到了<input type="file" name="f"> 这样的组件
                        /* String fieldName = item.getFieldName();
                         System.out.println("上传组件的名称: " + fieldName);
*/
                         //得到上传文件的名字
                        String name=item.getName();
                        name = name.substring(name.lastIndexOf("\\") + 1);
                        System.out.println(name);

                        String string = item.getString();
                        System.out.println(string);

                        //上传文件
                        // 获取上传文件内容,完成文件上传操作
                         InputStream is = item.getInputStream();
                         byte[] b = new byte[1024];
                         int len = -1;
                         //判断是上传的封面还是视频
                        //从而放到不同的储存文件夹中
                        //正则表达式判断是video还是image
                        String videoRegex = "video/[a-z|A-Z|0-9]*";
                        String imageRegex="image/[a-z|A-Z|0-9]*";
                        String type = item.getContentType();
                        System.out.println(type);
                        System.out.println("1");

                        if (type.matches(imageRegex)){
                             videoCover="http://menghong.x2.natapp.link/videoCover/"+name;
                            FileOutputStream fos=new FileOutputStream(imageUploadPath+name);
                            while((len=is.read(b))!=-1){
                                fos.write(b);
                            }
                            fos.close();
                            is.close();
                        }
                        else if(type.matches(videoRegex)) {
                            videoURL = "http://menghong.x2.natapp.link/video/" + name;
                            System.out.println("2");
                            FileOutputStream fos = new FileOutputStream(videoUploadPath + name);
                            while ((len = is.read(b)) != -1) {
                                //System.out.println(new String(b,0,len));
                                fos.write(b, 0, len);
                            }

                            fos.close();
                            is.close();
                        }
                    }
                }
            }
            //把信息放入数据库
            Connection con=new DBConnction().getConnction();
            String sql="INSERT INTO videos (title,videoURL,Did,coverURL) VALUES (?,?,?,?)";
            try {
                PreparedStatement pst=con.prepareStatement(sql);
                pst.setString(1,title);
                System.out.println(videoURL);
                pst.setString(2,videoURL);
                pst.setInt(3,Did);
                pst.setString(4,videoCover);
                int result=pst.executeUpdate();
                if (result==1){
                    System.out.println("插入数据库成功");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (FileUploadException e) {
            e.printStackTrace();
        }

    }
}
