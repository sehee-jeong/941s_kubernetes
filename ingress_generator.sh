#!/bin/bash

# ����ڷκ��� �Է� ���� �ޱ�
user_id=$1
service_name=$2
output_file=$3
port=$4

# ����� �Է� ���ڰ� ��� �����ϴ��� Ȯ��
if [[ -z $user_id || -z $service_name || -z $output_file || -z $port ]]; then
  echo "����: $0 [����� ID] [���񽺸�] [��� ���ϸ�] [������ ��Ʈ ��ȣ]"
  exit 1
fi

# Ingress YAML ���� ����
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

echo "Ingress YAML ������ �����Ǿ����ϴ�: $output_file"
