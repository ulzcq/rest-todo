package com.example.resttodo.controller;

import com.example.resttodo.dto.ResponseDTO;
import com.example.resttodo.dto.TodoDTO;
import com.example.resttodo.model.TodoEntity;
import com.example.resttodo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

  @Autowired
  private TodoService service;

  @GetMapping("/test")
  public ResponseEntity<?> testTodo() {
    String str = service.testService(); // 테스트 서비스 사용
    List<String> list = new ArrayList<>();
    list.add(str);
    ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
    return ResponseEntity.ok().body(response);
  }

  @PostMapping
  public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
    try {
      // (1) TodoDTO -> TodoEntity 변환
      TodoEntity entity = TodoDTO.toEntity(dto);
      entity.setId(null); //(2) 생성 당시에는 id가 없어야함

      // (3) 아이디 설정 - 인증 인가시 수정
      String temporaryUserId = "temporary-user";
      entity.setUserId(temporaryUserId);

      // (4) Todo 생성
      List<TodoEntity> entities = service.create(entity);

      // (5) entity -> TodoDTO
      List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

      // (6) TodoDTO -> ResponseDTO 초기화
      ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

      // (7) ResponseDTO를 리턴
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      // 예외가 나는 경우 dto대신 error에 메시지를 넣어 리턴
      String error = e.getMessage();
      ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
      return ResponseEntity.badRequest().body(response);
    }
  }

  @GetMapping
  public ResponseEntity<?> retrieveTodoList(){
    String temporaryUserId = "temporary-user";
    // (1) get todo list
    List<TodoEntity> entities = service.retrieve(temporaryUserId);

    // (2) entity -> TodoDTO
    List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

    // (3) TodoDTO -> ResponseDTO 초기화
    ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

    //(4) ResponseDTO를 리턴
    return ResponseEntity.ok().body(response);
  }

  @PutMapping
  public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
    String temporaryUserId = "temporary-user";
    // (1) TodoDto -> entity 변환
    TodoEntity entity = TodoDTO.toEntity(dto);
    entity.setUserId(temporaryUserId); // (2) 아이디 설정 - 인증 인가시 수정

    // (3) entity update
    List<TodoEntity> entities = service.update(entity);

    // (4) entity -> TodoDTO
    List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

    // (5) TodoDTO -> ResponseDTO 초기화
    ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

    // (6) ResponseDTO를 리턴
    return ResponseEntity.ok().body(response);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
    try {
      String temporaryUserId = "temporary-user"; // temporary user id.
      // (1) TodoDTO -> TodoEntity 변환
      TodoEntity entity = TodoDTO.toEntity(dto);
      entity.setUserId(temporaryUserId); // (2) 아이디 설정 - 인증 인가시 수정

      // (3) entity delete
      List<TodoEntity> entities = service.delete(entity);

      // (4) entity -> TodoDTO
      List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

      // (5) TodoDTO -> ResponseDTO 초기화
      ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

      // (6) ResponseDTO를 리턴
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      // (8) 예외가 나는 경우 dto대신 error에 메시지를 넣어 리턴
      String error = e.getMessage();
      ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
      return ResponseEntity.badRequest().body(response);
    }
  }


}
