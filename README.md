# 🎮 라이엇 통합 게임 커뮤니티 서비스

라이엇 게임을 이용하는 사용자들이 게임 정보를 얻고 소통할 수 있는 서비스 

## 프로젝트 기능 및 설계

- 라이엇 게임 전적 검색 기능
  - 사용자는 로그인과 관계없이 라이엇 게임의 닉네임을 검색 할 수 있다.
  - 유저의 최근 전적, 순위권, 티어별로 검색을 할 수 있다.

- 회원가입 기능
  - 사용자는 회원가입을 할 수 있다. 일반적으로 모든 사용자는 회원가입시 USER 권한 (일반 권한)을 지닌다. 
  - 회원가입시 아이디와 패스워드를 입력받으며, 아이디는 unique 해야한다. 

- 로그인 기능
  - 사용자는 로그인을 할 수 있다. 로그인시 회원가입때 사용한 아이디와 패스워드가 일치해야한다.
 
- 회원 프로필 기능
  - 사용자는 자신의 프로필을 조회하고 수정할 수 있다.
  - 회원이 프로필에 남긴 라이엇 아이디에 의해 각종 게임들의 정보가 프로필에 남는다.

- 게시글 작성 기능 
  - 로그인한 사용자는 권한에 관계 없이 글을 작성, 수정할 수 있다. 
  - 사용자는 게시글 제목(텍스트), 게시글 내용(텍스트)를 작성할 수 있다.
  - 게시글 작성자와 일치하는 사용자는 게시글을 삭제할 수 있다.

- 게시글 목록 조회 기능 
  - 로그인하지 않은 사용자를 포함한 모든 사용자는 게시글을 조회할 수 있다. 
  - 게시글은 최신순으로 기본 정렬되며, 댓글이 많은순/적은순 으로도 정렬이 가능하다.
  - 게시글 목록 조회시 응답에는 게시글 제목과 작성일, 댓글 수의 정보가 필요하다.
  - 게시글은 종류가 많을수 있으므로 paging 처리를 한다. 

- 특정 게시글 조회 기능
  - 로그인하지 않은 사용자를 포함한 모든 사용자는 게시글을 조회할 수 있다. 
  - 게시글 제목, 게시글 내용, 작성자, 작성일이 조회된다. 

- 댓글 작성 기능
  - 로그인한 사용자는 권한에 관계 없이 댓글을 작성할 수 있다. 
  - 사용자는 댓글 내용(텍스트)를 작성할 수 있다.
  - 댓글 작성자와 일치하는 사용자는 댓글을 삭제할 수 있다.
 
- 좋아요 추천 기능
  - 로그인한 사용자는 댓글에 좋아요, 추천을 할 수 있다. 
 


## ERD 
![777](https://github.com/Raccoonmen/Riot_Gaming/assets/108695511/85b5b7c9-4b25-4100-adb2-6aab0cea5870)


## Trouble Shooting


### Tech Stack
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>
