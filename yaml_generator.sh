#!/bin/bash

# 인자 확인
if [ $# -lt 5 ]; then
  echo "사용법: $0 <사용자ID> <서비스명> <출력파일> <이미지> <포트1> [<포트2> <포트3>] [<환경변수1>=<값1> [<환경변수2>=<값2> ...]]"
  exit 1
fi

# 사용자 입력 인자 저장
user_id=$1
service_name=$2
output_file=$3
image=$4
shift 4
ports=()
env_vars=()

# 포트 인자 처리
for ((i=1; i<=3; i++)); do
  if [ $# -gt 0 ]; then
    ports+=($1)
    shift
  fi
done

# 환경변수 인자 처리
while [ $# -gt 0 ]; do
  env_vars+=($1)
  shift
done

# 현재 날짜와 시간을 기반으로 파일명 생성
timestamp=$(date +"%Y%m%d%H%M%S")
output_file="${output_file}_${timestamp}.yaml"

# 서비스명을 MD5 해시로 변환하여 파일명 생성
service_name_md5=$(echo -n "$service_name" | md5sum | awk '{print $1}')
output_file="$service_name_md5.yaml"

# Namespace 설정
namespace=$user_id

# YAML 내용 작성
yaml_content=$(cat <<EOF
apiVersion: v1
kind: Namespace
metadata:
  name: $namespace
---
apiVersion: v1
kind: Service
metadata:
  name: $service_name-svc
  namespace: $namespace
spec:
  selector:
    app: $service_name
  type: LoadBalancer
  ports: \n
EOF
)

# 포트 설정 추가
for port in "${ports[@]}"; do
  yaml_content+="  - name: $service_name-port-$port\n    protocol: TCP\n    port: $port\n    targetPort: $port\n"
done

yaml_content+="\n---\n"

yaml_content+="apiVersion: apps/v1
kind: Deployment
metadata:
  name: $service_name
  namespace: $namespace
spec:
  selector:
    matchLabels:
      app: $service_name
  template:
    metadata:
      labels:
        app: $service_name
    spec:
      containers:
      - name: $service_name
        image: $image
        ports:\n"

# 포트 설정 추가
for port in "${ports[@]}"; do
  yaml_content+="        - containerPort: $port\n"
done

# 환경변수 설정 추가
for var in "${env_vars[@]}"; do
  key="${var%=*}"
  value="${var#*=}"
  yaml_content+="        env:\n"
  yaml_content+="        - name: $key\n          value: $value\n"
done

# YAML 파일 생성
echo -e "$yaml_content" > "$output_file"

echo "YAML 파일이 생성되었습니다: $output_file"
