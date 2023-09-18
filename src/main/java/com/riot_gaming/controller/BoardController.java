package com.riot_gaming.controller;

import com.riot_gaming.model.Board;
import com.riot_gaming.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/board")
public class BoardController {

  @Autowired
  private BoardService boardService;

  @GetMapping("/write")
  @PreAuthorize("hasRole('WRITE')")
  public String boardWriteForm() {

    return "boardwrite";
  }

  @PostMapping("/writepro")
  public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception{

    boardService.write(board, file);

    model.addAttribute("message", "글 작성이 완료되었습니다.");
    model.addAttribute("searchUrl", "/board/list");
    return "message";
  }

  @GetMapping("/list")
  public String boardList(Model model,
      @PageableDefault(page = 0, size = 10, sort = "id",
          direction = Sort.Direction.DESC) Pageable pageable,
      String searchKeyword) {

    Page<Board> list = null;

    if(searchKeyword == null) {
      list = boardService.boardList(pageable);
    }else {
      list = boardService.boardSearchList(searchKeyword, pageable);
    }

    int nowPage = list.getPageable().getPageNumber() +1;
    int startPage = Math.max(nowPage -4, 1);
    int endPage = Math.min(nowPage + 5, list.getTotalPages());

    model.addAttribute("nowPage", nowPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("list", list);

    return "boardlist2";
  }

  @GetMapping("/view")  // localhost:8080/board/view?id=1
  public String boardView(Model model, Integer id) {

    model.addAttribute("board", boardService.boardView(id));
    return "boardview";
  }

  @GetMapping("/delete")
  public String boardDelete(Integer id) {
    boardService.boardDelete(id);

    return "redirect:/list";
  }

  @GetMapping("/modify/{id}")
  public String boardModify(@PathVariable("id") Integer id,
      Model model) {

    model.addAttribute("board", boardService.boardView(id));

    return "boardmodify";
  }

  @PostMapping("/update/{id}")
  public String boardModify(@PathVariable("id") Integer id, Board board, MultipartFile file) throws Exception {

    Board boardTemp = boardService.boardView(id);
    boardTemp.setTitle(board.getTitle());
    boardTemp.setContent(board.getContent());

    boardService.write(boardTemp, file);

    return "redirect:/list";
  }


}
