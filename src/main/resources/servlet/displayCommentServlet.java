import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "displayCommentServlet" , value = "/displayCommentServlet")
public class displayCommentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        Connection con=new DBConnction().getConnction();
        PrintWriter out=response.getWriter();

        int videoID=Integer.parseInt(request.getParameter("videoID"));

        String sql1="SELECT * FROM comments where videoID = ? AND Fid = 0;";
        try {
            PreparedStatement pst=con.prepareStatement(sql1);
            pst.setInt(1,videoID);
            ResultSet resultSet=pst.executeQuery();
            while (resultSet.next()){
                String comment=resultSet.getString("comment");
                String username=resultSet.getString("username");
                String tousername=resultSet.getString("tousername");
                String calendar= String.valueOf(resultSet.getTimestamp("calendar"));
                int id=Integer.parseInt(resultSet.getString("id"));
                int Fid=Integer.parseInt(resultSet.getString("Fid"));
                String begin="{"+
                        "\"comment\":\""+comment+"\","+
                        "\"username \":\""+username+"\","+
                        "\"tousername \":\""+tousername+"\","+
                        "\" calendar\":\""+calendar+"\","+
                        "\"id\":"+id+","+
                        "\"Fid \":"+Fid+","+
                        "\"commentSon\": [";
                String commentSon=Comment.display(id);
                String end="]}";
                String all=begin+commentSon+end;

                out.print(all);
                System.out.println(all);
            }
            out.flush();
            out.close();
            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
