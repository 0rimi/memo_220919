package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@RestController
public class PostRestController {
	
	@Autowired
	private PostBO postBO;
	
	/**
	 * 글쓰기 화면
	 * @param subject
	 * @param content
	 * @param file
	 * @param session
	 * @return
	 */
	@PostMapping("/create")
	public Map<String,Object> create(
			@RequestParam("subject") String subject,
			@RequestParam(value="content", required=false) String content,
			@RequestParam(value="file",required=false) MultipartFile file,
			HttpSession session){
		
		int userId = (int)session.getAttribute("userId");
		String userLoginId = (String)session.getAttribute("userLoginId");
		
		// db insert
		int rowCnt = postBO.addPost(userId, userLoginId, subject, content, file);
		
		Map<String,Object> result = new HashMap<>();
		
		if(rowCnt > 0) {
			result.put("code", 1);
			result.put("result", "성공");
		}else {
			result.put("code", 500);
			result.put("errorMessage", "파일이 저장되지 않았습니다.");
		}
		
		return result;
	}
	
	/**
	 * 글수정
	 * @param postId
	 * @param subject
	 * @param content
	 * @param file
	 * @param session
	 * @return
	 */
	@PutMapping("/update")
	public Map<String,Object> update(
			@RequestParam("postId") int postId,
			@RequestParam("subject") String subject,
			@RequestParam(value="content", required = false) String content,
			@RequestParam(value="file",required = false) MultipartFile file,
			HttpSession session
			){
		
		int userId = (int)session.getAttribute("userId");
		String userLoginId = (String)session.getAttribute("userLoginId");
		
		// db update
		postBO.updatePost(userId, userLoginId, subject, postId, content, file);
		
		//result
		Map<String,Object> result = new HashMap<>();
		result.put("code", 1);
		result.put("result", "성공");
		
		return result;
	}
	
	@DeleteMapping("/delete")
	public Map<String,Object> delete(
			@RequestParam("postId") int postId,
			HttpSession session){
		
		int userId = (int)session.getAttribute("userId");
		
		//db delete
		int row = postBO.deletePostByPostIdUserId(postId, userId);
		
		//result
		Map<String,Object> result = new HashMap<>();
		if(row>0) {
			result.put("code", 1);
			result.put("result", "성공");
		}else {
			result.put("code",500);
			result.put("errorMessage", "메모 삭제에 실패했습니다. 관리자에게 문의해주세요.");
		}
		
		return result;
	}

}
