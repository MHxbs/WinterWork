import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
@WebServlet(name = "CommentServlet" , value = "/CommentServlet")
public class CommentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        HttpSession session=request.getSession();
        if (session.getAttribute("username")!=null) {

            DBConnction dbConnction = new DBConnction();
            Connection con = dbConnction.getConnction();
            PrintWriter out = response.getWriter();

            String comment=request.getParameter("comment");
            String username=(String)session.getAttribute("username");
            String tousername=request.getParameter("tousername");
            Timestamp calendar=Timestamp.valueOf(request.getParameter("calendar"));

            int Fid=Integer.parseInt(request.getParameter("Fid"));
            int videoID=Integer.parseInt(request.getParameter("videoID"));



            try {
                String sql = "INSERT INTO comments (comment,username,tousername,calendar,Fid,videoID) VALUES " +
                        "(?,?,?,?,?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, comment);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, tousername);
                preparedStatement.setTimestamp(4, calendar);
            /*preparedStatement.setInt(5,comment.getId());*/
                preparedStatement.setInt(5, Fid);
                preparedStatement.setInt(6, videoID);
                int q = preparedStatement.executeUpdate();
                if (q == 1) {
                    out.print("{\"result\" : \"评论成功\"}");
                    System.out.println("插入评论成功");
                } else {
                    out.print("{\"result\" : \"评论失败\"}");
                    System.out.println("插入评论失败");
                }
                out.flush();
                out.close();
                preparedStatement.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {

            PrintWriter out=response.getWriter();
            System.out.println("你未登录，不能评论");
            //out.print("{\"result\" : \"你还没登陆，请登录\"}");
            out.flush();
            out.close();
        }
    }
}