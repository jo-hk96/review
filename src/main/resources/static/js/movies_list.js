// ==================== 전역 변수 ====================
let currentPage = 1;
let isLoading = false;
let totalPages = 500;
let currentCategory = 'top_rated'; // 기본 카테고리
let movieListContainer;
let loadingIndicator;

// ==================== 검색 실행 ====================
async function fetchAndRenderSearchResults(query) {
    if (!query) return;

    const url = `/api/movies/search?query=${encodeURIComponent(query)}`;

    if (loadingIndicator) loadingIndicator.style.display = 'block';

    try {
        const response = await fetch(url);
        const searchResults = await response.json();

        movieListContainer.innerHTML = '';

        if (response.ok && searchResults && searchResults.length > 0) {
            renderMovies(searchResults);
        } else {
            movieListContainer.innerHTML = `<p>'${query}'에 대한 검색 결과가 없습니다.</p>`;
        }
    } catch (error) {
        console.error("검색 오류:", error);
        movieListContainer.innerHTML = '<p>검색 서버와 연결할 수 없습니다.</p>';
    } finally {
        if (loadingIndicator) loadingIndicator.style.display = 'none';
    }
}

// form 제출 시 실행되는 함수
/*function handleMovieSearch(event) {
   event.preventDefault(); // 새로고침 막기

     const movieSearchInput = document.getElementById('movieSearch');
    const query = movieSearchInput ? movieSearchInput.value.trim() : '';

    if (!query) {
        alert("검색할 영화 제목을 입력해주세요.");
        return;
    }
    
    // ⭐️⭐️ 핵심: URL에 검색어를 포함하여 페이지 이동을 강제합니다. ⭐️⭐️
    const url = `/MoviesList?movieSearch=${encodeURIComponent(query)}`; 
    window.location.href = url;
}*/

// ==================== 영화 카드 렌더링 ====================
function renderMovies(newMovies) {
    newMovies.forEach(movie => {
        const posterUrl = movie.poster_path
            ? `https://image.tmdb.org/t/p/w500${movie.poster_path}`
            : '/img/no-poster.png'; // fallback 이미지
        const movieId = movie.id;
        const detailUrl = `/detail/${movieId}`;

        const card = document.createElement('div');
        card.className = 'movie-card2';
        card.innerHTML = `
          <a href="${detailUrl}" class="movie-link">
              <img src="${posterUrl}" alt="${movie.title} 포스터">
                  <div class="movie-info2">
                        <h2>${movie.title}</h2>
                        <p>외부평점: ${movie.vote_average ? movie.vote_average.toFixed(1) : 'N/A'} / 10</p>
                        <p>최초개봉날짜: ${movie.release_date || '정보 없음'}</p>
                  </div>
          </a>
        `;
        movieListContainer.appendChild(card);
    });
}

// ==================== 목록 API 호출 ====================
async function loadMovies(page) {
    if (isLoading || page > totalPages) return;
    isLoading = true;
    loadingIndicator.style.display = 'block';

    const options = {
        method: 'GET',
        headers: {
            accept: 'application/json',
            Authorization: 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyYmEwZmM1NDdkZGI5ZDA3ZGQ0ODhkZmRmOTEzZmZiZCIsIm5iZiI6MTc1ODc1ODkyMy44MzUsInN1YiI6IjY4ZDQ4ODBiNTRjYWJjY2VjYzRhOTFjNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.xDFPD2BRvK_XT3ITjx-q9u31nL4PJ-Y0w8MsLeNgiyg' // 꼭 교체
        }
    };

    try {
        const url = `https://api.themoviedb.org/3/movie/${currentCategory}?language=ko-KR&page=${page}`;
        const response = await fetch(url, options);
        const data = await response.json();

        totalPages = data.total_pages;
        const newMovies = data.results || [];
        currentPage = page;

        renderMovies(newMovies);
    } catch (err) {
        console.error("API 호출 중 오류 발생:", err);
    } finally {
        isLoading = false;
        loadingIndicator.style.display = 'none';
    }
}

// ==================== 무한 스크롤 ====================
function handleInfiniteScroll() {
    const documentHeight = document.documentElement.scrollHeight;
    const currentScrollPosition = window.scrollY + window.innerHeight;
    const isNearBottom = currentScrollPosition >= documentHeight - 100;

    if (isNearBottom) {
        if (currentPage < totalPages && !isLoading) {
            console.log(`[Infinite Scroll] 페이지 ${currentPage + 1} 로드 시작`);
            loadMovies(currentPage + 1);
        } else if (currentPage >= totalPages && !isLoading && loadingIndicator.textContent !== '마지막 페이지입니다.') {
            loadingIndicator.textContent = '마지막 페이지입니다.';
            loadingIndicator.style.display = 'block';
        }
    }
}

// ==================== 카테고리 변경 ====================
function changeCategory(newCategory) {
    if (currentCategory === newCategory) return;

    currentCategory = newCategory;
    currentPage = 1;
    totalPages = 500;

    movieListContainer.innerHTML = '';
    loadingIndicator.textContent = '영화 로드 중...';
    loadingIndicator.style.display = 'none';

    loadMovies(currentPage);
}

// ==================== 초기 실행 ====================
document.addEventListener('DOMContentLoaded', () => {
    movieListContainer = document.getElementById('movie-list2');
    loadingIndicator = document.getElementById('loading-indicator2');

    if (!movieListContainer || !loadingIndicator) {
        console.error("ERROR: 필수 HTML 요소를 찾을 수 없어 JS 실행을 중단합니다.");
        return;
    }

	const searchQueryElement = document.getElementById('searchQueryInput');
    const G_SEARCH_QUERY = searchQueryElement ? searchQueryElement.value : '';

    // 검색어가 주어졌을 때 (서버에서 주입된 경우)
    if (typeof G_SEARCH_QUERY !== 'undefined' && G_SEARCH_QUERY) {
		
		//검색모드
		console.log("검색 모드 실행, 쿼리:", G_SEARCH_QUERY);
        fetchAndRenderSearchResults(G_SEARCH_QUERY);

        const categorySelectDiv = document.getElementById('movie-categories-select');
        if (categorySelectDiv) {
            categorySelectDiv.style.display = 'none';
        }
    } else {
        // 기본 목록 모드
        console.log("기본 목록 모드 실행");
        window.addEventListener('scroll', handleInfiniteScroll);
        loadMovies(currentPage);
    }

    // 카테고리 변경 이벤트
    const categorySelect = document.getElementById('category-select');
    if (categorySelect) {
        categorySelect.addEventListener('change', (event) => {
            const newCategory = event.target.value;
            if (newCategory) {
                changeCategory(newCategory);
            }
        });
    }
});
