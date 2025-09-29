    
    
    
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
   
	//수정된 renderMovieDetail 함수 (감독, 배우 추가 로직 포함)
	function renderMovieDetail(data) {
	    const detailContainer = document.getElementById('movie-detail-container');
	    const basePosterUrl = "https://image.tmdb.org/t/p/w500"; 
	    const baseBackdropUrl = "https://image.tmdb.org/t/p/w1280"; 
	    
	    //장르
	    const genres = data.genres.map(g => g.name).join(', ') || '정보 없음';
	    
	    //기획,제작사
	    const production_companies = data.production_companies.map(p => p.name).join(', ');
	    
	    //영화 뒷배경
	    const backdropImage = data.backdrop_path 
	        ? `${baseBackdropUrl}${data.backdrop_path}` 
	        : '';
		//제작국가
		const production_countries = data.production_countries.map(co => co.name).join(', ');
	    
	    // 1. 감독 정보 추출
	    // job이 'Director'인 crew만 찾는다
	    const directors = data.credits.crew.filter(c => c.job === 'Director');
	
	
	    // 2. 주연 배우 5명 추출 (cast 배열의 order는 출연 비중에 따라 정렬됨)
	    // 최대 5명만 잘라서 사용한다.
	    const topCast = data.credits.cast.slice(0, 5); 
	    // 주연 배우들을 HTML 문자열로 만든다.
	    const castHTML = topCast.map(actor => `
	        <div style="text-align: center; width: 100px;">
	            <img src="${basePosterUrl}${actor.profile_path}"alt="${actor.name}" 
	                 style="width: 100px; height: 150px; object-fit: cover; border-radius: 8px;">
	            <p style="margin: 5px 0 0; font-size: 0.9em;"><strong>${actor.name}</strong></p>
	            <p style="margin: 0; font-size: 0.8em; color: #777;">(${actor.character})</p>
	        </div>
	    `).join('');
	    
	    // 주연 배우들을 HTML 문자열로 만든다.
	    const directorHTML = directors.map(directors => `
	        <div style="text-align: center; width: 100px;">
	            <img src="${basePosterUrl}${directors.profile_path}"alt="${directors.name}" 
	                 style="width: 100px; height: 150px; object-fit: cover; border-radius: 8px;">
	            <p style="margin: 5px 0 0; font-size: 0.9em;"><strong>${directors.name}</strong></p>
	            <p style="margin: 0; font-size: 0.8em; color: #777;">(${directors.job})</p>
	        </div>
	    `).join('');
	    
	    // ----------------------------------------------------
	    
	    detailContainer.innerHTML = `
	         <div class="backdrop-header" 
	             style="background-image: url('${backdropImage}'); 
	                    height: 400px; 
	                    background-size: cover; 
	                    background-position: center;
	                    display: flex; align-items: flex-end; padding: 20px; 
	                    color: white; text-shadow: 1px 1px 5px rgba(0,0,0,0.8);
	                    ${backdropImage}">
	            <header>
	                <h1>${data.title}</h1>
	                <p><strong>원제:</strong> (${data.original_title})</p>
	                <p>⭐️ ${data.vote_average.toFixed(1)} / 10</p>
	            </header>
	        </div>
	
	        <section style="display: flex; gap: 30px; margin-top: 20px;">
	            <img src="${basePosterUrl}${data.poster_path}" alt="${data.title} 포스터" style="width: 300px; border-radius: 8px;">
	            <div style="flex-grow: 1;">
	                <h2>줄거리</h2>
	                <p>${data.overview || '줄거리 정보 없음'}</p>
	                
	                <h3>기본 정보</h3>
	                <p><strong>개봉일:</strong> ${data.release_date}</p>
	                <p><strong>러닝타임:</strong> ${data.runtime}분</p>
	                <p><strong>제작국가:</strong> ${production_countries}</p>
	                <p><strong>장르:</strong> ${genres}</p>
	                <p><strong>기획/제작:</strong> ${production_companies}</p>
	                <p><strong>태그라인:</strong> <em>${data.tagline || '태그 정보 없음'}</em></p>
	            </div>
	        </section>
	
	        <hr style="margin: 40px 0;">
	        <h2>감독 / 주요 출연진</h2>
	        <div style="display: flex; gap: 15px; overflow-x: auto;">
	            ${directorHTML} ${castHTML || '<p>출연진 정보가 없습니다.</p>'}
	        </div>
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
	    
	    // 1. 영화 상세 정보 요청 URL
	    const detailUrl = `https://api.themoviedb.org/3/movie/${movieId}?language=ko-KO`;
	    // 2. 출연진/제작진 정보 요청 URL (네가 추가하려던 그 부분!)
	    const creditsUrl = `https://api.themoviedb.org/3/movie/${movieId}/credits?language=ko-KO`;
	
	    // Promise.all로 두 요청을 병렬로 처리
	    Promise.all([
	        fetch(detailUrl, options).then(res => res.json()),
	        fetch(creditsUrl, options).then(res => res.json())
	    ])
	    .then(([detailData, creditsData]) => {
	        // 두 결과를 하나의 객체로 합친다
	        const combinedData = {
	            ...detailData, // 상세 정보
	            credits: creditsData // 출연진/제작진 정보 추가
	        };
	        
	        // 합쳐진 데이터를 렌더링 함수에 전달
	        renderMovieDetail(combinedData); 
	    })
	    .catch(err => {
	        console.error("API 호출 실패:", err);
	        document.getElementById('movie-detail-container').innerHTML = `
	            <p>영화 정보를 불러오는 데 실패했습니다. (${err.message})</p>
	        `;
	    });
	}