import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "searchServlet" ,value = "/searchServlet")
public class searchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out =resp.getWriter();
        Connection con=new DBConnction().getConnction();
        String search=req.getParameter("search");
        String Fsearch="%"+search+"%";
        System.out.println(Fsearch);
        try {

            String sql="SELECT * FROM videos where title LIKE  ? ";
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setString(1,Fsearch);
            ResultSet resultSet= pst.executeQuery();
            int a=0;
            String total="{ ";
            while(resultSet.next()){
                a++;
                String title=resultSet.getString("title");
                String url=resultSet.getString("videoURL");
                String id=resultSet.getString("id");
                String Did=resultSet.getString("Did");
                String coverURL=resultSet.getString("coverURL");

                String all="\"video"+a+"\" :   {\"title\" : \" "+title+"\",\"url\" : \""+url+"\",\"id\" : "+id+",\"Did\" : "+Did+
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
            out.flush();
            out.close();
            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
