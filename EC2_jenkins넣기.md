# EC2 인스턴스에 jenkins 설치하가

## 1. EC2 인스턴스 생성하기
1. 사용할 운영체제는 ubuntu로 선택
2. 키 페어 생성하기 키유형 RSA, 프라이빗 키 파일 형식 .pem으로 
3. 보안 그룹 규칙에 8080포트 추가하기
4. SSH로 접속

## 2. UFW 활성화
- 처음 EC2에 접속하면, 리눅스가 완전 디폴트 상태기 때문에, 리눅스에서 기본 제공하는 apt 패키지를 update 해줘야 한다.
```
sudo apt-get update
sudo ufw enable
sudo ufw reload
```

## 3. 도커 설치하기
```
sudo apt-get install docker.io
```

## 4. jenkins 컨테이너 생성
docker container에 마운트할 볼륨 디렉토리 생성
```
cd /home/ubuntu $$ mkdir jenkins-data
```

```
sudo docker run -d -p 8080:8080 -v /home/ubuntu/jenkins-data:/var/jenkins_home --name jenkins jenkins/jenkins:lts
```

구동 상태를 보기 위해 로그 출력
- 초기 패스워드는 기록해두기
```
sudo docker logs jenkins

```


