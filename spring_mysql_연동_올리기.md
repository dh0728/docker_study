# 🐋 Docker spring boot mysql 연동

Docker에서 spring boot 와 mysql을 연동하는 방법
도커에서 spring boot와 mysql을 연동하여 배포하려면 **spring boot 컨테이너**와 **mysql 컨테이너**가 각각 필요

두 컨테이너를 연동하는 방법
- 브릿지 네트워크 사용
- docker compose 사용
- host 네트워크 공유

나는 **브릿지 네트워크(bridge network)**를 생성해서 사용하는 방법을 다를 예정 (도커의 기본 네트워크 모드가 브릿지 네트워크)
- Docker 네트워크를 생성
- spring boot 이미지와 mysql 이미지를 해당 네트워크를 통해 실행하여 컨테이너를 생성

## 🐬1.1 docker network 생성

```
docker network create docker-network
```
터미널 창에 **docker network create docker-network** 입력
- **docker network ls** 명령어 입력 후 docker 네트워크 목록 확인

![alt text](image6.png)

## 🐬 2.1 mysql 이미지 컨테이너 생성
- 터미널 창에 **docker pull mysql:8.0** 입력
- docker images로 확인 가능

![alt text](image7.png)

## 🐬 2.2 mysql docker 컨테이너 생성
다운로드한 mysql docker 이미지를 생성한 docker 네트워크에 귀속시키며 생성

```
docker run -d --name mysql-container --network docker-network -e MYSQL_ROOT_PASSWORD=1234 -p 3306:3306 mysql:8.0
```

- docker run : 새로운 컨테이너를 생성하고 실행
- -d 백그라운드 실행. 터미널에서 실행된 채 유지되지 않고 백그라운드에서 실행
- --name mysql-container: 생성할 컨테이너 이름을 mysql-container로 실행
- --network docker-network: 컨테이너를 미리 생성한 docker-network 네트워크에 연결
- -e MYSQL_ROOT_PASSWORD=1234 : 환경 변수 설정, MySQL의 root 계정 비밀번호 1234로 설정
- mysql:8.0 : 사용할 MySQL 이미지와 버전을 지정

![alt text](image8.png)

### 🐟 docker 생성중 발생하는 오류
![alt text](image10.png)
- **호스트(로컬)의 3306포트가 이미 사용 중**이라 MySQL 컨테이너를 생성할 때 포트를 바인딩할 수 없을 때 발생하는 오류이다.
- 참고로 해당 오류가 발생해도 container의 생성은 된다. run이 안될뿐..

#### 해결 방법
해당 포트가 사용 중인지 확인후 종료해야 한다.
- 프로세스 종료는 관리자 권한으로 cmd를 실행후 삭제해야 한다.

**Windows에서 3306 포트가 사용 중인지 확인**
```
netstat -ano | findstr :3306
```

**해당 프로세스 종료**
```
taskkill /PID <프로세스 ID(PID)> /F

// 종료휴 run 하자
docker start mysql-container
```

실제 해당 컨테이너가 좀 전에 생성한 docker-network에 속하는지 확인
```
docker network inspect docker-network
```

![alt text](image9.png)

## 🐬 2.3 mysql DB 생성
spring boot project에서는 DB의 이름은 helloDev로 해서 사용할 예정
현재 도커의 mysql에는 해당 DB가 없어 생성해야 함.

```
docker exec -it mysql-container mysql -u root -p

# 생성할 때 입력한 비밀번호 입력
Enter password: 1234

mysql> create database helloDev;

mysql> use helloDev;

mysql> CREATE TABLE test_table (
    id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO test_table (name) VALUES ('홍길동');

SELECT * FROM test_table;

```
docker exec -it <mysql-container이름> mysql -u root -p 명령어를 통해 DB 생성


## 🐬 3. Spring boot 컨테이너 생성
1. Dockerfile 생성
2. 이미지 생성
3. 컨테이너 생성
- spring 컨테이너 생성은 스프링_부트_도커배포해보기참고

#### Spring boot 이미지 생성
```
./gradlew build

docker build -t spring . 
```

#### Spring boot 컨테이너 생성
```
 docker run -d --name spring-container --network docker-network -p 8088:8088 spring
```

#### spring boot, mysql 컨테이너가 생성한 네트워크에 속하는지 확인.
docker network inspect docker-network


## 🐬 호출해서 값 잘 나오는지 확인~~
- **포트 번호 확인 잘하기!!!**
```
http://127.0.0.1:8088/api/testTable
```

![alt text](image11.png)


🚀 왜 네트워크 연결이 필요한가?
1. 컨테이너는 기본적으로 서로 격리되어 있음
    - 각 컨테이너는 독립적인 환경에서 실행되므로 기본적으로 다른 컨테이너와 통신할 수 없음
    - localhost를 사용하면 같은 컨테이너 내에서만 접근 가능하고, 다른 컨테이너로는 연결되지 않음




### 출처

https://tytydev.tistory.com/45