package edu.du.teamportpoilo_pyl.controller;

import edu.du.teamportpoilo_pyl.dto.BoardDto;
import edu.du.teamportpoilo_pyl.dto.CommentDto;
import edu.du.teamportpoilo_pyl.service.BoardService;
import edu.du.teamportpoilo_pyl.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    public BoardController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/list")
    public String boardList(@RequestParam(defaultValue = "1") int page, Model model) {
        int size = 10;
        model.addAttribute("board", boardService.getAllBoardsByPage(page, size));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", boardService.getTotalPages(size));
        return "/board/boardList";
    }

    @GetMapping("/board/{id}")
    public String viewBoard(@PathVariable Long id, @ModelAttribute CommentDto commentDto, Model model, HttpSession httpSession) {
        model.addAttribute("board", boardService.getBoardById(id));
        model.addAttribute("comments", commentService.getAllComments(id));
        return "/board/viewBoard";
    }

    @GetMapping("/board/{id}/edit")
    public String editBoardForm(@PathVariable Long id, @ModelAttribute BoardDto boardDto, Model model) {
        model.addAttribute("board", boardService.getBoardById(id));
        return "/board/editBoard";
    }

    @PostMapping("/board/{id}/edit")
    public String editBoard(@PathVariable Long id, @ModelAttribute BoardDto boardDto, Model model) {
        boardService.updateBoard(boardDto, id);
        return "redirect:/board/" + id;
    }

    @PostMapping("/board/{id}/comment/write")
    public String updateComment(@PathVariable Long id, @ModelAttribute CommentDto commentDto) {
        commentService.insertComment(commentDto);
        return "redirect:/board/" + id;
    }

    @PostMapping("/board/{id}/comment/delete")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long id,
                                                             @ModelAttribute CommentDto commentDto,
                                                             HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String nameSession = (String) session.getAttribute("name");

        if (!commentDto.getAuthor().equals(nameSession)) {
            response.put("success", false);
            response.put("message", "게시글 삭제 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        commentService.deleteComment(commentDto.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/board/write")
    public String writeBoard() {
        return "/board/writeBoard";
    }

    @PostMapping("/board/write")
    public String writeBoard(@ModelAttribute BoardDto boardDto) {
        boardService.insertBoard(boardDto);
        return "redirect:/list";
    }

    @PostMapping("board/{id}/like")
    public String likeBoard(@PathVariable Long id) {
        boardService.likeBoard(id);
        return "redirect:/board/" + id;
    }

    @PostMapping("board/{id}/delete")
    public String deleteBoard(@PathVariable Long id, HttpSession session) {
        boardService.deleteBoard(id);
        return "redirect:/list";
    }

}
