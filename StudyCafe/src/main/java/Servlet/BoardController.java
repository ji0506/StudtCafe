package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Board;
import model.Comment;
import model.Kategorie;
import service.BoardService;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String ARTICEL_IMAGE_REPO = "D:\\file_repo";

	
	BoardService brdService;
	
	public void init(ServletConfig config) throws ServletException {
		brdService = new BoardService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");
		HttpSession session;
		String action = request.getPathInfo();

        if(request.getSession().getAttribute("userId") == null) {
            response.sendRedirect("/member/main.do");
            return;
        }

		
		List<Kategorie> katlist  = brdService.getMenu();
		request.setAttribute("katlist", katlist);

		
		String strKatNo = request.getParameter("katNo");
		int katNo = -1;
		if(strKatNo != null) {
			katNo = Integer.parseInt(strKatNo);
			request.setAttribute("katTargetNo", katNo);
		}
			
		for(Kategorie kat : katlist)
			if(katNo == kat.getKateNo())
				request.setAttribute("katTargetName", kat.getKateName());

		
		try {
			List<Board> list = null;
			
			if(action == null) {
				String _section = request.getParameter("setion");
				String _pageNum = request.getParameter("pageNum");
				String searchId = request.getParameter("searchId");
				String searchText = request.getParameter("searchText");

				
				//페이징처리
				int section = Integer.parseInt(((_section == null) ? "1" : _section));
				int pageNum = Integer.parseInt(((_pageNum == null) ? "1" : _pageNum));

				Map<String,Object> pagingMap = new HashMap<String,Object>();
				pagingMap.put("section", section);
				pagingMap.put("pageNum", pageNum);
				pagingMap.put("katNo", katNo);
				pagingMap.put("searchId", searchId);
				pagingMap.put("searchText", searchText);

				if(searchId != null)
					list  = brdService.getBoardSearchList(pagingMap);
				else 
					list  = brdService.getBoardList(pagingMap);

				request.setAttribute("section", section);
				request.setAttribute("pageNum", pageNum);
				request.setAttribute("list", list);
				 			
				nextPage = "/view/board.jsp";
			} else if("/list.do".equals(action)) {

				String _section = request.getParameter("setion");
				String _pageNum = request.getParameter("pageNum");
				String searchId = request.getParameter("searchId");
				String searchText = request.getParameter("searchText");

				
				//페이징처리
				int section = Integer.parseInt(((_section == null) ? "1" : _section));
				int pageNum = Integer.parseInt(((_pageNum == null) ? "1" : _pageNum));
				
				Map<String,Object> pagingMap = new HashMap<String,Object>();
				pagingMap.put("section", section);
				pagingMap.put("pageNum", pageNum);
				pagingMap.put("katNo", katNo);
				pagingMap.put("searchId", searchId);
				pagingMap.put("searchText", searchText);

				
				list  = brdService.getBoardList(pagingMap);

				request.setAttribute("section", section);
				request.setAttribute("pageNum", pageNum);
				request.setAttribute("list", list);

				if(list.size() > 0)
					request.setAttribute("tot", list.get(0).getTotalCount());


				nextPage = "/view/board.jsp";

			} else if("/searchlist.do".equals(action)) {

				String _section = request.getParameter("setion");
				String _pageNum = request.getParameter("pageNum");
				String searchId = request.getParameter("searchId");
				String searchText = request.getParameter("searchText");

				
				//페이징처리
				int section = Integer.parseInt(((_section == null) ? "1" : _section));
				int pageNum = Integer.parseInt(((_pageNum == null) ? "1" : _pageNum));
				
				Map<String,Object> pagingMap = new HashMap<String,Object>();
				pagingMap.put("section", section);
				pagingMap.put("pageNum", pageNum);
				pagingMap.put("katNo", katNo);
				pagingMap.put("searchId", searchId);
				pagingMap.put("searchText", searchText);

				
				list  = brdService.getBoardSearchList(pagingMap);

				request.setAttribute("section", section);
				request.setAttribute("pageNum", pageNum);
				request.setAttribute("list", list);

				if(list.size() > 0)
					request.setAttribute("tot", list.get(0).getTotalCount());


				nextPage = "/view/list.jsp";

			} else if(action.equals("/Form.do")) {
				nextPage = "/view/modaltest.jsp";
				
			} else if (action.equals("/add.do")){
				String title = request.getParameter("title");
				String content = request.getParameter("content");
				//String imageFileName = request.getParameter("imageFileName");
				System.out.println(title + "," + content);

				Board brd =new Board();
				brd.setTitle(title);
				brd.setContent(content);
				brd.setKateNo(katNo);
				brd.setUserId((String)request.getSession().getAttribute("userId"));
				
				brdService.save(brd);
				nextPage = "/board/list.do";
			}else if (action.equals("/save.do")){
				String title = request.getParameter("title");
				String content = request.getParameter("content");
				String brdNo = request.getParameter("brdNo");

				//String imageFileName = request.getParameter("imageFileName");
				System.out.println(title + "," + content);

				Board brd =new Board();
				brd.setTitle(title);
				brd.setContent(content);
				brd.setKateNo(katNo);
				brd.setUserId((String)	request.getSession().getAttribute("userId"));
				brd.setBrdNo(Integer.parseInt(brdNo));
				brdService.save(brd);
				nextPage = "/board/list.do";
			} else if(action.equals("/view.do")){
				String no = request.getParameter("brdNo");
				Board vo = brdService.getBoardView(Integer.parseInt(no));
				request.setAttribute("info", vo);
				brdService.CntUpdate(vo.getBrdNo(),vo.getCnt()+1);
				vo.setCnt(vo.getCnt()+1);
				List<Comment> comlist= brdService.getCommentList(Integer.parseInt(no));
				
				request.setAttribute("list", comlist);

				nextPage = "/view/view.jsp";
				
			} else if(action.equals("/mod.do")) {// 글 수정 부분 여기에 추가해 주세요
				String title = request.getParameter("title");
				String content = request.getParameter("content");
				String brdNo = request.getParameter("brdNo");

				Board fix = brdService.getBoardView(Integer.parseInt(brdNo));
				request.setAttribute("info", fix);
				
				request.setAttribute("update", fix);
				nextPage = "/view/viewmod.jsp";
				
			} else if(action.equals("/remove.do")){  // 게시판 삭제								  
			    String str = request.getParameter("brdNo");
			    
			    int brdNo = Integer.parseInt(str);
			    
			    brdService.removeBoard(brdNo);   
			    nextPage="/board/list.do";
				
				
			} else if (action.equals("/replyForm.do")) {// 댓글 쓰는 기능 여기에 추가해 주세요
				
				
				
				nextPage = "/board/view.do";
			} else if (action.equals("/addReply.do")) {	//댓글 추가 부분 여기에 추가해주세요
				int brdNo = Integer.parseInt(request.getParameter("brdNo"));
				String content = request.getParameter("content");

				Comment comment = new Comment();
				
				comment.setBrdNo(brdNo);
				comment.setComContent(content);
				comment.setUserId((String)request.getSession().getAttribute("userId"));

				brdService.saveComment(comment);
				
				request.setAttribute("brdNo", brdNo);
				
				nextPage = "/board/view.do";
			} else if ("/Updatevote.do".equals(action)) {
				String voteNO = request.getParameter("voteNo");
				String brdNo = request.getParameter("brdNo");
				
				brdService.VoteUpdate(Integer.parseInt(brdNo),Integer.parseInt(voteNO)+1);
				
				return;
			} 
			

			RequestDispatcher dis = request.getRequestDispatcher(nextPage);
			dis.forward(request, response);
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}

