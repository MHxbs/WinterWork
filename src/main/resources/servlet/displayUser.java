import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class displayUser extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();

        if (session.getAttribute("username")==null){
           response.sendRedirect("html/login_failure.jsp");
           return;
        }
        //得到数据库链接
        Connection con=new DBConnction().getConnction();
        String sql="SECLCT * FROM users WHERE username = \'"+session.getAttribute("username")+"\'";
        try {
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet resultSet=pst.executeQuery();
            if (resultSet.next()){
                String username =resultSet.getString("username");
                String headSculputre=resultSet.getString("headSculpture");
                int level=resultSet.getInt("level");
                PrintWriter out=response.getWriter();
                out.print("{\"username\" : \""+username+"\",\"headSculpture\":\""+headSculputre+"\"" +
                        ",\"level\":"+level+"}");
                out.flush();
                out.close();
                pst.close();
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
