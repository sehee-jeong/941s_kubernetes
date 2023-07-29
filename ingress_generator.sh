#!/bin/bash

# 사용자로부터 입력 인자 받기
user_id=$1
service_name=$2
output_file=$3
port=$4

# 사용자 입력 인자가 모두 존재하는지 확인
if [[ -z $user_id || -z $service_name || -z $output_file || -z $port ]]; then
  echo "사용법: $0 [사용자 ID] [서비스명] [출력 파일명] [서비스의 포트 번호]"
  exit 1
fi

# Ingress YAML 파일 생성
cat <<EOF >$output_file
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: $service_name-ingress
  namespace: $service_name
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
  - host: $user_id.example.com
    http:
      paths:
      - path: /$service_name/path1
        pathType: Prefix
        backend:
          service:
            name: ${service_name}-svc
            port:
              number: $port
EOF

echo "Ingress YAML 파일이 생성되었습니다: $output_file"
