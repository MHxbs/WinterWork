import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "landOutServlet",value = "/landOutServlet")
public class landOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        session.invalidate();
        PrintWriter out=response.getWriter();
        out.print("<%--\n" +
                "  Created by IntelliJ IDEA.\n" +
                "User: asuspc\n" +
                "Date: 2018/3/8\n" +
                "Time: 19:14\n" +
                "To change this template use File | Settings | File Templates.\n" +
                "--%>\n" +
                "<%@ page contentType=\"text/html;charset=UTF-8\" language=\"java\" %>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>failure</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "退出成功！\n" +
                "</body>\n" +
                "</html>\n");
    }
}
