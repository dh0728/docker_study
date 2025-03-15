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

sudo ufw allow 22/tcp
sudo ufw allow 8080/tcp

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

docker pull jenkins/jenkins:lts

sudo docker run -d -p 8080:8080 -v /home/ubuntu/jenkins-data:/var/jenkins_home --name jenkins jenkins/jenkins:lts
```

구동 상태를 보기 위해 로그 출력
- 초기 패스워드는 기록해두기
```
sudo docker logs jenkins

```


## 5. 환경 설정 변경

- jenkins data 폴더로 이동
```
cd jenkins-data
```

- updata center에 필요한 CA 파일을 다운로드한다

```
mkdir update-center-rootCAs

wget https://cdn.jsdelivr.net/gh/lework/jenkins-update-center/rootCA/update-center.crt -O ./update-center-rootCAs/update-center.crt
```

- jenkins의 default 설정에서 특정 미러사이트로 대체하도록 아래 명령어를 실행
    - 아래 명령을 수행후 hudson.model.UpdateCenter.xml 파일을 열러 https://raw.githubusercontent.com/lework/jenkins-update-center/master/updates/tencent/update-center.json이 URL로 변경되었는지 확인인

```
 sudo sed -i 's#https://updates.jenkins.io/update-center.json#https://raw.githubusercontent.com/lework/jenkins-update-center/master/updates/tencent/update-center.json#' ./hudson.model.UpdateCenter.xml

sudo docker restart jenkins

nano /home/ubuntu/jenkins-data/hudson.model.UpdateCenter.xml

```

## 설치된 버전 확인 
- 설치된 버전이 낮아서 플러그인이 다 설치가 안되는거 같다 버전을 확인하고 업데이트 시켜보자

- 현재 사용자를 docker 그룹에 추가
```
sudo usermod -aG docker $USER
exit
groups

docker exec -it jenkins /bin/bash

```
- 
docker exec -it jenkins /bin/bash

jenkins.install.UpgradeWizard.state 파일에서 확인
```
cat /var/jenkins_home/jenkins.install.UpgradeWizard.state

## 실행결과 2.492.1로 플러그인을 모두 다운받기 위해서는 2.499를 다운받아야 한단다.
```