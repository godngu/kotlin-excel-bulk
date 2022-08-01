package com.godngu.excel.bulk.board

import org.springframework.data.jpa.repository.JpaRepository

interface BoardEntityRepository: JpaRepository<BoardEntity, Long>
