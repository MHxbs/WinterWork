import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "registerServlet" , value = "/registerServlet")
public class registerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        //判断username全是数字吗
        String regex="[0-9]*";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(username);
        //全是数字
        if (matcher.matches()) {

            DBConnction dbConnction = new DBConnction();
            Connection con = dbConnction.getConnction();
            PrintWriter out = response.getWriter();
            try {
                //判断是否已经有该用户名
                String sql = "SELECT username FROM users where username = ? ";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, username);
                ResultSet resultSet = pst.executeQuery();
                if (resultSet.next()) {

                    //out.print("{\"how\" : 0}");
                    out.print("<html>\n" +
                            "<body>\n" +
                            "<h2>已经有该用户名!</h2>\n" +
                            "</body>\n" +
                            "</html>\n");
                    System.out.println("已经有该用户了");
                } else {
                    //没有该用户名，插入数据
                    String sql1 = "INSERT INTO users (username,password,level) VALUES (?,?,?)";
                    PreparedStatement pst2 = con.prepareStatement(sql1);
                    pst2.setString(1, username);
                    pst2.setString(2, password);
                    pst2.setInt(3,0);
                    int result = pst2.executeUpdate();
                    if (result == 1) {
                        //out.print("{\"how\" : 1}");
                        out.print("<html>\n" +
                                "<body>\n" +
                                "<h2>注册成功!</h2>\n" +
                                "</body>\n" +
                                "</html>\n");
                        System.out.println("注册成功！");
                        pst2.close();
                    } else {
                        //response.sendRedirect("register_failure.jsp");
                        out.print("<html>\n" +
                                "<body>\n" +
                                "<h2>插入数据库失败</h2>\n" +
                                "</body>\n" +
                                "</html>\n");
                    }
                }
                out.flush();
                out.close();
                resultSet.close();
                pst.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{//输入的用户名不是数字
            PrintWriter out=response.getWriter();
            out.print("<html>\n" +
                    "<body>\n" +
                    "<h2>输入的用户名必须是数字！</h2>\n" +
                    "</body>\n" +
                    "</html>\n");

            // out.print("{\"how\" : 0 }");
            System.out.println("输入的数字必须是数字");
            out.flush();
            out.close();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
