// ==================== 전역 변수 ====================
let currentPage = 1;
let isLoading = false;
let totalPages = 500; // 기본 카테고리
let movieListContainer;
let loadingIndicator;
let currentCategory = 'popular'; 
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

// ==================== 영화 카드 렌더링 ====================
function generateOurStars(rating5) {
    if (rating5 === null || rating5 === undefined || rating5 === 0) return 'N/A';
    const fullStars = Math.floor(rating5);
    return '⭐'.repeat(fullStars);
}

function renderMovies(newMovies) {
    newMovies.forEach(movie => {
       		//api 컨트롤러에서 보낸 평점을 받음 
          const ourRating = movie.ourAverageRating; // <--- 이 값이 정상적으로 들어와야 함!
          const movieId = movie.id;
          const detailUrl = `/detail/${movieId}`; 
             const posterUrl = movie.poster_path
            ? `https://image.tmdb.org/t/p/w500${movie.poster_path}`
            : '/img/no-poster.png'; // fallback 이미지 처리 포함

        // 평점을 별 아이콘과 포맷된 텍스트로 준비
        const userStars = generateOurStars(ourRating);
        const scoreText = ourRating > 0 ? ourRating.toFixed(1) : 'N/A';


        const card = document.createElement('div');
        card.className = 'movie-card2';
        card.innerHTML = `
          <a href="${detailUrl}" class="movie-link">
              <img src="${posterUrl}" alt="${movie.title} 포스터">
                  <div class="movie-info2">
                        <h2>
                            ${movie.title}
                            
                           ${scoreText !== 'N/A' ? ` 
                            <span style="font-size: 0.7em; font-weight: normal; display: block; margin-top: 5px;">
                                ${userStars} (${scoreText} / 5)
                            </span>
                           ` : `<span style="font-size: 0.7em; font-weight: normal; display: block; margin-top: 5px;">
                                평점없음
                           	 </span>`} 
                        </h2>
                        
                        <p>외부평점: ${movie.vote_average ? movie.vote_average.toFixed(1) : '외부평점없음'} / 10</p>
                        <p>최초개봉날짜: ${movie.release_date || '정보 없음'}</p>
                  </div>
          </a>
        `;
        movieListContainer.appendChild(card);
    });
}

// ==================== 목록 API 호출 ====================
async function loadMovies(page) { // ⭐ page 파라미터 다시 사용 ⭐

 	const pageToLoad = page > 0 ? page : 1; 
    if (isLoading || page > totalPages) return;
    isLoading = true;
    loadingIndicator.style.display = 'block';

    // 페이지 번호를 파라미터로 넘김
    const url = `/api/movies/list?category=${currentCategory}&page=${pageToLoad}`; 

    try {
        const response = await fetch(url);
        const data = await response.json(); 
        
        totalPages = data.total_pages; 
        const newMovies = data.results || [];
        currentPage = page;

        renderMovies(newMovies); 
    } catch (err) {
        console.error("API 호출 중 오류 발생:", err);
        movieListContainer.innerHTML = '<p>영화 목록을 불러오는 중 서버 오류가 발생했습니다.</p>';
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

function changeCategory(newCategory) {
    // 검색 모드가 아닐 때만 카테고리 변경을 허용 (선택적)
    // 이 로직이 필요하다면 유지합니다.
    const searchQueryInput = document.getElementById('searchQueryInput');
    if (searchQueryInput && searchQueryInput.value) {
        console.warn("검색 모드에서는 카테고리 변경이 제한됩니다.");
        return; 
    }
    
    console.log(`카테고리 변경: ${currentCategory} -> ${newCategory}`);
    
    // 1) 새로운 카테고리 값으로 전역 변수 업데이트
    currentCategory = newCategory;
    
    // 2) 페이지 상태 초기화
    currentPage = 1;
    totalPages = 1; // totalPages도 초기화
    
    // 3) 기존 목록을 비우고 다시 로드
    if (movieListContainer) movieListContainer.innerHTML = ''; 
    
    // 4) 로드 시작
    loadMovies(currentPage); 
    
    // 무한 스크롤 이벤트 리스너가 붙어 있지 않다면 다시 붙임
    window.removeEventListener('scroll', handleInfiniteScroll);
    window.addEventListener('scroll', handleInfiniteScroll);
}





// ==================== 카테고리 변경 ====================
document.addEventListener('DOMContentLoaded', () => {
    // 필수 요소 초기화
    movieListContainer = document.getElementById('movie-list2'); // ⭐ ID를 'movie-list'로 가정
    loadingIndicator = document.getElementById('loading-indicator2');

    if (!movieListContainer || !loadingIndicator) {
        console.error("ERROR: 필수 HTML 요소를 찾을 수 없어 JS 실행을 중단합니다.");
        return;
    }

    const searchQueryElement = document.getElementById('searchQueryInput');
    const G_SEARCH_QUERY = searchQueryElement ? searchQueryElement.value : '';
    const categorySelect = document.getElementById('category-select');


    // 1. 카테고리 드롭다운 초기 상태 설정
    if (categorySelect) {
        // 서버에서 받은 값으로 드롭다운 초기 선택 설정
        categorySelect.value = currentCategory;
        
        // 카테고리 변경 이벤트 설정
        categorySelect.addEventListener('change', (event) => {
            changeCategory(event.target.value);
        });
    }

    // 2. 검색 모드와 기본 목록 모드 분기
    if (typeof G_SEARCH_QUERY !== 'undefined' && G_SEARCH_QUERY) {
        // 검색 모드
        console.log("검색 모드 실행, 쿼리:", G_SEARCH_QUERY);
        fetchAndRenderSearchResults(G_SEARCH_QUERY);

        // 카테고리 선택 UI 숨기기
        const categorySelectDiv = document.getElementById('movie-categories-select');
        if (categorySelectDiv) {
            categorySelectDiv.style.display = 'none';
        }
    } else {
          // ⭐ 기본 목록 모드 실행. 이제 카테고리 구분은 없습니다. ⭐
        console.log("기본 목록 모드 실행.");
        window.addEventListener('scroll', handleInfiniteScroll);
    }
    
     if (!(typeof G_SEARCH_QUERY !== 'undefined' && G_SEARCH_QUERY)) {
        // ⭐ 카테고리가 아닌 기본 목록을 로드합니다. ⭐
        loadMovies();
    }
});