    
    
    
    //함수정의
    document.addEventListener('DOMContentLoaded', () => {
        // 1. 현재 URL 경로를 가져옴 (예: "/detail/1007734")
        const path = window.location.pathname; 

        // 2. 경로를 '/' 기준으로 쪼개고, 빈 문자열을 제거해서 배열을 깔끔하게 만듦
        // 예: ["detail", "1007734"]
        const cleanParts = path.split('/').filter(Boolean); 

        let movieId = null;

        // 3. ID는 배열의 '마지막' 요소에 있을 것으로 예상
        if (cleanParts.length > 0) {
            const lastPart = cleanParts[cleanParts.length - 1];
            // 마지막 요소가 숫자인지 확인해서 ID로 확정
            if (!isNaN(lastPart)) { 
                movieId = lastPart;
            }
        }
        
        const detailContainer = document.getElementById('movie-detail-container');

        // 4. 추출된 ID로 API 호출
        if (movieId) {
            console.log("추출된 영화 ID:", movieId);
            fetchMovieDetail(movieId); 
        } else {
            console.error("URL에서 유효한 영화 ID를 추출하지 못했습니다.");
            detailContainer.innerHTML = "영화 ID를 찾을 수 없습니다.";
        }
    });
    
    
    // 3. 영화 상세 정보를 HTML로 표시하는 함수
    function renderMovieDetail(data) {
        const detailContainer = document.getElementById('movie-detail-container');
        
        // 포스터 경로 (TMDB 기준)
        const basePosterUrl = "https://image.tmdb.org/t/p/w500"; 
        
        // 장르들을 쉼표로 연결
        const genres = data.genres.map(g => g.name).join(', ');
        
        
        const production_companies = data.production_companies.map(p => p.name).join(', ');

        // 템플릿 리터럴로 상세 정보 구성
        detailContainer.innerHTML = `
            <header>
                <h1>${data.title}</h1>
                <p><strong>원제:</strong> (${data.original_title})</p>
                <p>⭐️ ${data.vote_average.toFixed(1)} / 10</p>
            </header>

            <section style="display: flex; gap: 30px; margin-top: 20px;">
                <img src="${basePosterUrl}${data.poster_path}" alt="${data.title} 포스터" style="width: 300px; border-radius: 8px;">
                <div>
                    <h2>줄거리</h2>
                    <p>${data.overview}</p>
                    
                    <h3>기본 정보</h3>
                    <p><strong>개봉일:</strong> ${data.release_date}</p>
                    <p><strong>러닝타임:</strong> ${data.runtime}분</p>
                    <p><strong>장르:</strong> ${genres}</p>
                    <p><strong>기획/제작:</strong> ${production_companies}</p>
                    <p><strong>태그라인:</strong> <em>${data.tagline || '정보 없음'}</em></p>
                </div>
            </section>
        `;
    }
    
    function fetchMovieDetail(movieId) {
        const options = {
            method: 'GET',
            headers: {
                accept: 'application/json',
                
                Authorization: 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyYmEwZmM1NDdkZGI5ZDA3ZGQ0ODhkZmRmOTEzZmZiZCIsIm5iZiI6MTc1ODc1ODkyMy44MzUsInN1YiI6IjY4ZDQ4ODBiNTRjYWJjY2VjYzRhOTFjNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.xDFPD2BRvK_XT3ITjx-q9u31nL4PJ-Y0w8MsLeNgiyg' 
            }
        };
        
        const url = `https://api.themoviedb.org/3/movie/${movieId}?language=ko-KO`;

        fetch(url, options)
            .then(res => {
                if (!res.ok) {
                    throw new Error(`HTTP 오류: ${res.status}`);
                }
                return res.json();
            })
            .then(data => {
                renderMovieDetail(data); // 성공 시 화면 그리는 함수 호출
            })
            .catch(err => {
                console.error("API 호출 실패:", err);
                document.getElementById('movie-detail-container').innerHTML = `
                    <p>영화 정보를 불러오는 데 실패했습니다. (${err.message})</p>
                `;
            });
    }