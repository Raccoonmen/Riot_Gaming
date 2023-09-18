// 로그인 요청 함수
function login(username, password) {
  fetch('/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ username, password })
  })
  .then(response => response.json())
  .then(data => {
    const token = data.token;
    // 로그인 성공 시 서버로부터 받은 토큰을 저장
    saveTokenToLocalStorage(token);
    // 로그인 성공 메시지 표시 및 리디렉트 또는 다른 작업 수행
    handleLoginSuccess();
  })
  .catch(error => {
    console.error('로그인 오류:', error);
  });
}

// 토큰을 로컬 스토리지에 저장하는 함수
function saveTokenToLocalStorage(token) {
  localStorage.setItem('token', token);
}

// HTTP 요청을 보낼 때 헤더에 토큰을 추가하는 함수
function sendHttpRequestWithToken(url, method, data) {
  // 로컬 스토리지에서 토큰을 가져옴
  const token = localStorage.getItem('token');

  // HTTP 요청 헤더 설정
  const headers = {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };

  // HTTP 요청 객체 생성
  const requestOptions = {
    method,
    headers,
    body: JSON.stringify(data)
  };

  // HTTP 요청 보내기
  fetch(url, requestOptions)
  .then(response => {
    if (!response.ok) {
      throw new Error('HTTP Error');
    }
    return response.json();
  })
  .then(data => {
    // 요청 성공 시 처리
    console.log(data);
  })
  .catch(error => {
    // 요청 실패 시 처리
    console.error('Error:', error);
  });
}

// 토큰을 로컬 스토리지에서 가져오는 함수
function getTokenFromLocalStorage() {
  return localStorage.getItem('token');
}

// 예제 사용
login('your_username', 'your_password'); // 로그인 요청
sendHttpRequestWithToken('/api/some-endpoint', 'GET', null); // 토큰이 필요한 엔드포인트로 요청
