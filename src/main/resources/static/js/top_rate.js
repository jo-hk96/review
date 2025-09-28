		let currentMovieIndex = 0;
		let currentPage = 1;
		let isLoading = false;
		let totalPages = 500;
		let allMovies = [];
		
		const movieListContainer = document.getElementById('movie-list');
		const prevBtn = document.getElementById('prev-btn');
		const nextBtn = document.getElementById('next-btn');
		
		function getItemsPerView() {
			  const width = window.innerWidth;
			  if (width <= 400) return 1;
			  if (width <= 600) return 2;
			  if (width <= 900) return 3;
			  if (width <= 1200) return 4;
			  return 5;
			}
		
		function renderMovies(newMovies) {
		  newMovies.forEach(movie => {
		    const posterUrl = `https://image.tmdb.org/t/p/w500${movie.poster_path}`;
		    
		    const movieId = movie.id; 
    		const detailUrl = `/detail/${movieId}`;
		    
		    const card = document.createElement('div');
		    card.className = 'movie-card';
		    card.innerHTML = `
		      <a href="${detailUrl}" class="movie-link">
			      <img src="${posterUrl}" alt="${movie.title} 포스터">
			      <div class="movie-info">
			        <h2>${movie.title}</h2>
			        <p>외부평점: ${movie.vote_average.toFixed(1)} / 10</p>
			        <p>최초개봉일: ${movie.release_date}</p>
			      </div>
			   </a>
		    `;
		    movieListContainer.appendChild(card);
		  });
		
		  // 새 영화 추가 후 위치/버튼 상태 업데이트
		  updateSlidePosition();
		  setTimeout(updateSlidePosition, 50);
		}
		
		function updateSlidePosition() {
		  const firstCard = movieListContainer.querySelector('.movie-card');
		  if (!firstCard || allMovies.length === 0) {
		    prevBtn.disabled = true;
		    nextBtn.disabled = true;
		    return;
		  }
		  const gap = 20; // CSS와 동일하게 유지
		  const itemsPerView = getItemsPerView();
		  const cardWidth = firstCard.offsetWidth;
		  const offset = (cardWidth + gap) * currentMovieIndex;
		
		  movieListContainer.style.transform = `translateX(-${offset}px)`;
		
		  const lastIndex = Math.max(0, allMovies.length - itemsPerView);
		  prevBtn.disabled = currentMovieIndex <= 0;
		  nextBtn.disabled = (currentMovieIndex >= lastIndex && currentPage >= totalPages) || isLoading;
		}
		
		async function loadMovies(page) {
		  if (isLoading || page > totalPages) return;
		  isLoading = true;
		  document.getElementById('loading-indicator').style.display = 'block';
		
		  const options = {
  method: 'GET',
  headers: {
    accept: 'application/json',
    Authorization: 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyYmEwZmM1NDdkZGI5ZDA3ZGQ0ODhkZmRmOTEzZmZiZCIsIm5iZiI6MTc1ODc1ODkyMy44MzUsInN1YiI6IjY4ZDQ4ODBiNTRjYWJjY2VjYzRhOTFjNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.xDFPD2BRvK_XT3ITjx-q9u31nL4PJ-Y0w8MsLeNgiyg'
  }
};

		  try {
		    const url = `https://api.themoviedb.org/3/movie/top_rated?language=ko-KR&page=${page}`;
		    const response = await fetch(url, options);
		    const data = await response.json();
		
		    totalPages = data.total_pages;
		    const newMovies = data.results || [];
		    allMovies = allMovies.concat(newMovies);
		    currentPage = page;
		
		    renderMovies(newMovies);
		  } catch (err) {
		    console.error("API 호출 중 오류 발생:", err);
		  } finally {
		    isLoading = false;
		    document.getElementById('loading-indicator').style.display = 'none';
		  }
		}
		
		prevBtn.addEventListener('click', () => {
		  if (currentMovieIndex > 0) {
		    currentMovieIndex--;
		    updateSlidePosition();
		  }
		});
		
		nextBtn.addEventListener('click', () => {
		  const itemsPerView = getItemsPerView();
		  const lastMovableIndex = Math.max(0, allMovies.length - itemsPerView);
		
		  if (currentMovieIndex < lastMovableIndex) {
		    currentMovieIndex++;
		    updateSlidePosition();
		    console.log(`[SLIDE] 인덱스 증가: ${currentMovieIndex}`);
		  } else {
		    // 더 로드할 페이지가 있으면 로드 시도
		    if (currentPage < totalPages && !isLoading) {
		      console.log(`[API_LOAD_FORCED] 다음 페이지 ${currentPage + 1} 로드 요청`);
		      loadMovies(currentPage + 1);
		    } else {
		      console.warn(`[STOP] 더 이상 이동 불가 (index ${currentMovieIndex}, last ${lastMovableIndex})`);
		    }
		  }
		});
		
		// 리사이즈 시 항목 수 변경 -> 인덱스 보정 및 위치 업데이트
		window.addEventListener('resize', () => {
		  const itemsPerView = getItemsPerView();
		  const maxIndex = Math.max(0, allMovies.length - itemsPerView);
		  if (currentMovieIndex > maxIndex) currentMovieIndex = maxIndex;
		  updateSlidePosition();
		});
		
		// 초기 로드
		movieListContainer.innerHTML = '';
		currentMovieIndex = 0;
		loadMovies(currentPage); 
