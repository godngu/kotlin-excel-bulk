package com.godngu.excel.bulk

import com.godngu.excel.bulk.board.BoardEntity
import com.godngu.excel.bulk.board.BoardEntityRepository
import com.godngu.excel.bulk.board.BoardService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.PostConstruct

@RestController
class BoardController(
    private val boardEntityRepository: BoardEntityRepository,
    private val boardService: BoardService,
) {

//    @PostConstruct
//    fun bulkInsert() {
//        (1..100).forEach {
//            boardEntityRepository.save(BoardEntity("title_$it", "content_$it"))
//        }
//    }

    @GetMapping("/hello")
    fun hello() = "hello"

    @GetMapping("/")
    fun main() {
        boardService.excelDownload()
    }
}
