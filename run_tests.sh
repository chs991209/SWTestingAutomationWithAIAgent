#!/bin/bash

echo "========================================"
echo "🚀 AI Agent 기반 자동화: 로컬 테스트 및 자동 Push"
echo "========================================"

# Maven을 사용하여 이전 빌드 찌꺼기를 지우고 테스트 실행
mvn clean test

# 실행 결과(Exit Code)를 캡처하여 성공/실패 여부 자동 판별
RESULT=$?

echo "========================================"
if [ $RESULT -eq 0 ]; then
    echo "✅ [테스트 성공] 모든 단위 테스트가 통과되었습니다! (Green)"
    echo "🚀 코드를 GitHub 저장소에 자동으로 Push합니다..."
    
    # Git 상태를 확인하고 변경된 모든 파일을 스테이징
    git add .
    
    # 현재 시간 기반으로 커밋 메시지 자동 생성
    COMMIT_MSG="TDD: All tests passed on $(date +'%Y-%m-%d %H:%M:%S')"
    git commit -m "$COMMIT_MSG"
    
    # master 브랜치로 푸시
    git push origin master
    
    # Push 성공 여부 확인
    if [ $? -eq 0 ]; then
        echo "🎉 [업로드 완료] 코드가 성공적으로 GitHub에 Push되었습니다."
        echo "💡 이제 GitHub Actions가 트리거되어 서버 환경에서 다시 테스트를 진행합니다."
    else
        echo "❌ [업로드 실패] Git Push 중 오류가 발생했습니다. 권한이나 연결 상태를 확인하세요."
    fi

else
    echo "❌ [테스트 실패] 하나 이상의 테스트가 실패했습니다. (Red)"
    echo "🚫 코드에 결함이 있으므로 GitHub 업로드를 중단합니다."
    echo "💡 테스트 에러 로그를 확인하고 소스 코드를 수정해 주세요."
fi
echo "========================================"

# 스크립트 실행 결과를 Maven 테스트의 결과 코드와 동일하게 맞춰서 종료
exit $RESULT
