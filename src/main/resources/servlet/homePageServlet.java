import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "homePageServlet" , value = "/homePageServlet")
public class homePageServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        response.setContentType("text/html;charset=utf-8");
        //得到传来的Did
        int Did=Integer.parseInt(request.getParameter("Did"));
        Connection con=new DBConnction().getConnction();
        PrintWriter out=response.getWriter();
        String sql="SELECT * FROM videos where Did = ?";
        //json对象的开始
        /*String  total="{ \"videos\" : [";*/
        int a=0;
        String total="{ ";
        try {
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setInt(1,Did);
            ResultSet resultSet=pst.executeQuery();
            while (resultSet.next()){
                a++;
                String title=resultSet.getString("title");
                String videoURL=resultSet.getString("videoURL");
                String id=resultSet.getString("id");

                String coverURL=resultSet.getString("coverURL");

                String all="\"video"+a+"\" : {\"title\" : \" "+title+"\",\"url\" : \""+videoURL+"\",\"id\" : "+id+",\"Did\" : "+Did+
                        ",\"coverURL\" : \""+coverURL+"\""+ "}";

                if (total.length()>=all.length()-10) {
                    total = total + " , " + all;

                }else {
                    total+=all;
                }
            }
            total+=" }";
            out.print(total);
            System.out.println(total);
            System.out.println("分区"+a+"成功");
           /* total=total+" ] }";
            out.print(total);
            System.out.println(total);*/
            out.flush();
            out.close();
            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
