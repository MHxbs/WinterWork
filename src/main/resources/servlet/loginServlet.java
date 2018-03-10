import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@WebServlet(name = "loginServlet",value = "/loginServlet")
public class loginServlet extends HttpServlet {

    @Override

    protected void doGet(HttpServletRequest reqest, HttpServletResponse response) throws ServletException, IOException {
        doPost(reqest,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        //得到session
        HttpSession session=request.getSession();
        //判断session中是否已经有用户登陆了
        if (session.getAttribute("username")==null) {
            String username = request.getParameter("username");
            System.out.println(username);
            String password = request.getParameter("password");
            System.out.println(password);
            //用正则表达式判断输入的用户名是否全是数字
            String regex = "[0-9]*";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(username);

            if (matcher.matches()) {//输入的用户名全是数字
                DBConnction dbConnction = new DBConnction();
                Connection con = dbConnction.getConnction();
                PrintWriter out = response.getWriter();
                try {
                    //判断数据库中是否有该用户

                    String sql1 = "SELECT username,password FROM users where username= ?";
                    PreparedStatement pst = con.prepareStatement(sql1);
                    pst.setString(1, username);
                    ResultSet resultSet = pst.executeQuery();
                    if (resultSet.next()) {
                        String passwordFromMysql = resultSet.getString("password");
                        //有该用户则判断密码是否匹配
                        if (password.equals(passwordFromMysql)) {
                        //ut.print("{\"how\" : 2}");
                            out.print("<html>\n" +
                                    "<body>\n" +
                                    "<h2>登陆成功！</h2>\n" +
                                    "</body>\n" +
                                    "</html>\n");
                           // response.sendRedirect("comment.jsp");
                           // response.sendRedirect("html/upload.jsp");
                            System.out.println("登陆成功！");

                            session.setAttribute("username", username);
                        } else {
                            //out.print("{\"how\" : 1}");
                            out.print("<html>\n" +
                                    "<body>\n" +
                                    "<h2>密码错误!</h2>\n" +
                                    "</body>\n" +
                                    "</html>\n");
                            System.out.println("密码错误");
                        }
                    } else {//没有结果集，该用户名不存在
                        out.print("<html>\n" +
                                "<body>\n" +
                                "<h2>没有该账户!</h2>\n" +
                                "</body>\n" +
                                "</html>\n");
                        // out.print("{\"how\" : 0}");
                        System.out.println("没有该账户");
                    }
                    out.flush();
                    out.close();
                    resultSet.close();
                    pst.close();
                    con.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {//用户名输入的不是数字
                PrintWriter out = response.getWriter();
               // out.print("{\"how\" : 0 } ");
                out.print("<html>\n" +
                        "<body>\n" +
                        "<h2>输入的用户名必须是数字!</h2>\n" +
                        "</body>\n" +
                        "</html>\n");

                /*response.sendRedirect("login_failure.jsp");*/
                System.out.println("输入的用户名必须是数字");
                out.flush();
                out.close();
            }

        }else {//已经有用户登陆了
            PrintWriter out=response.getWriter();
            out.print("<html>\n" +
                    "<body>\n" +
                    "<h2>已有账户登陆，注销后才能登陆！</h2>\n" +
                    "</body>\n" +
                    "</html>\n");
        }
    }
}
