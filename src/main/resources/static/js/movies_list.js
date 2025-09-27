// ==========================================================
// 1. 전역 변수 초기화 (가장 먼저)
// ==========================================================
let currentPage = 1;
let isLoading = false;
let totalPages = 500;

// ⭐️ 현재 선택된 카테고리를 저장하는 변수 (기본값: now_playing)
let currentCategory = 'top_rated'; 

const movieListContainer = document.getElementById('movie-list2');
const loadingIndicator = document.getElementById('loading-indicator2'); 

// ==========================================================
// 2. 함수 정의 (사용할 함수를 먼저 정의)
// ==========================================================

// 1. 영화 카드를 렌더링하는 함수
function renderMovies(newMovies) {
  newMovies.forEach(movie => {
    const posterUrl = `https://image.tmdb.org/t/p/w500${movie.poster_path}`;
    const movieId = movie.id; 
    const detailUrl = `/detail/${movieId}`; 
    
    const card = document.createElement('div');
    card.className = 'movie-card2'; 
    card.innerHTML = `
      <a href="${detailUrl}" class="movie-link">
          <img src="${posterUrl}" alt="${movie.title} 포스터">
              <div class="movie-info2">
                    <h2>${movie.title}</h2>
                    <p>외부평점: ${movie.vote_average.toFixed(1)} / 10</p>
                    <p>최초개봉날짜: ${movie.release_date}</p>
              </div>
      </a>
    `;
    movieListContainer.appendChild(card);
  });
}

// 2. 영화 데이터를 로드하는 함수
async function loadMovies(page) {
  if (isLoading || page > totalPages) return;
  isLoading = true;
  loadingIndicator.style.display = 'block';

    const options = {
        method: 'GET',
        headers: {
            accept: 'application/json',
            Authorization: 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyYmEwZmM1NDdkZGI5ZDA3ZGQ0ODhkZmRmOTEzZmZiZCIsIm5iZiI6MTc1ODc1ODkyMy44MzUsInN1YiI6IjY4ZDQ4ODBiNTRjYWJjY2VjYzRhOTFjNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.xDFPD2BRvK_XT3ITjx-q9u31nL4PJ-Y0w8MsLeNgiyg'
        }
    };

  try {
    // ⭐️ currentCategory 변수를 사용하여 URL 생성 ⭐️
    const url = `https://api.themoviedb.org/3/movie/${currentCategory}?language=ko-KO&page=${page}`;
    
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

// 3. 무한 스크롤 감지 로직
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

// 4. 카테고리를 변경하고 목록을 새로고침하는 함수 (실행 로직)
function changeCategory(newCategory) {
    if (currentCategory === newCategory) return; 

    currentCategory = newCategory; // 👈 이 변수가 업데이트됩니다!
    currentPage = 1;               // 페이지 초기화
    totalPages = 500;              // 총 페이지 수 초기화
    
    // 화면과 로딩 인디케이터 초기화
    movieListContainer.innerHTML = ''; 
    loadingIndicator.textContent = '영화 로드 중...'; 
    loadingIndicator.style.display = 'none';
    
    loadMovies(currentPage); // 👈 새 카테고리로 첫 페이지 로드 시작
    
    // (여기에 select/a 태그의 활성화 스타일 변경 로직을 추가하면 됩니다.)
}


// ==========================================================
// 3. 이벤트 리스너 및 초기 실행 (가장 마지막)
// ==========================================================

// 스크롤 이벤트 리스너 추가 (무한 스크롤)
window.addEventListener('scroll', handleInfiniteScroll);

// 초기 로드 (페이지가 로드되자마자 첫 페이지를 불러옴)
loadMovies(currentPage); 


document.addEventListener('DOMContentLoaded', () => {
    const categorySelect = document.getElementById('category-select');
    
    // ⭐️ select 박스의 값이 변경(change)될 때 이벤트 처리 ⭐️
    if (categorySelect) {
        categorySelect.addEventListener('change', (event) => {
            const newCategory = event.target.value;
            if (newCategory) {
                // 선택된 값을 가지고 changeCategory 함수 호출!
                changeCategory(newCategory); 
            }
        });
    }
});

// 스크롤 이벤트 리스너 추가 (무한 스크롤)
window.addEventListener('scroll', handleInfiniteScroll);

// 초기 로드 (페이지가 로드되자마자 첫 페이지를 불러옴)
loadMovies(currentPage); 