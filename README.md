# 대용량 엑셀 Writer

## 왜 이렇게 만들었는가?

- DB 조회되는 데이터가 많은 경우 `List`로 받는 경우 OOM 발생 함.
- Excel로 Write 해야 하는 데이터가 많아서 시간이 오래 걸림

## 해결 방안은?

### OOM (Out Of Memory)

- MyBatis의 `ResultHandler`를 사용 해도 되지만 `JDBCTemplate`를 사용 하기로 했다.
- JDBCTemplate 에서는 `queryForStream`을 제공한다 -> 스크림 처리 가능

### 대용량 Excel Write

- Multi Thread를 이용해서 엑셀 데이터를 생성한다.
- 엑셀 데이터 생성시 `sheet.createRow(행 번호)` 규칙이 있으므로 rownum을 이용해서 멀리쓰레드 작업이 가능함
- poi의 SXSSF를 이용해서 일정 단위로 메모리 데이터를 파일로 내보낼 수 있다 -> 대용량 처리에 적합
