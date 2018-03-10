import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "lunboServlet" , value = "/lunboServlet")
public class lunboServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=response.getWriter();

        //1-音乐 2-动漫 3-鬼畜 4-舞蹈
        //得到分类的值
       int sort=Integer.parseInt(request.getParameter("Lid"));

        Connection con=new DBConnction().getConnction();
        try {

            String sql="SELECT * FROM videos where Lid = ? ";
            PreparedStatement preparedStatement=con.prepareStatement(sql);
            preparedStatement.setInt(1,sort);
            ResultSet resultSet=preparedStatement.executeQuery();
            String total="{ ";
            int a=0;
            while (resultSet.next()){
                a++;

                String title=resultSet.getString("title");
                String videoURL=resultSet.getString("videoURL");
                String id=resultSet.getString("id");
                String Did=resultSet.getString("Did");
                String coverURL=resultSet.getString("coverURL");
                String all="\"video"+a+"\" : "+ "{\"title\" : \" "+title+"\",\"url\" : \""+videoURL+"\",\"id\" : "+id+",\"Did\" : "+Did+
                        ",\"coverURL\" : \""+coverURL+"\""+ "}";

                if (total.length()>=all.length()-10) {
                    total = total + " , " + all;

                }else {
                    total+=all;
                }
            }
            total+=" }";
            System.out.println(total);
            out.print(total);
            out.flush();
            out.close();
            preparedStatement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
