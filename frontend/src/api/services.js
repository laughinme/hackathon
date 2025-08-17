import apiProtected from './axios';

export const getMyProfile = () => apiProtected.get('/users/me/');
export const updateUserProfile = (userData) => apiProtected.patch('/users/me/', userData);
export const updateUserGenres = (genreIds) => apiProtected.put('/users/me/genres', { favorite_genres: genreIds });
export const updateUserPicture = (formData) => apiProtected.put('/users/me/picture', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
});

export const getBooksForYou = (params) => apiProtected.get('/books/for_you', { params });
export const getAllBooks = (params) => apiProtected.get('/books/', { params });
export const getMyBooks = () => apiProtected.get('/books/my');
export const getBookById = (bookId) => apiProtected.get(`/books/${bookId}/`);
export const createBook = (bookData) => apiProtected.post('/books/create', bookData);
export const editBook = (bookId, bookData) => apiProtected.patch(`/books/${bookId}/`, bookData);
export const uploadBookPhotos = (bookId, formData) => apiProtected.put(`/books/${bookId}/photos`, formData, {
  headers: { 'Content-Type': 'multipart/form-data' },
});
export const likeBook = (bookId) => apiProtected.post(`/books/${bookId}/like`);
export const reserveBook = (bookId, data) => apiProtected.post(`/books/${bookId}/reserve`, data);
export const recordBookClick = (bookId) => apiProtected.post(`/books/${bookId}/click`);


export const getGenres = () => apiProtected.get('/books/genres/');
export const getAuthors = () => apiProtected.get('/books/authors/');
export const getLanguages = () => apiProtected.get('/languages/');

export const getCities = () => apiProtected.get('/geo/cities/');
export const getExchangeLocations = (filterByDistance = true) => apiProtected.get('/geo/exchange_locations/', { params: { filter: filterByDistance }});

export const getOwnedExchanges = (params) => apiProtected.get('/exchanges/owned', { params });
export const getRequestedExchanges = (params) => apiProtected.get('/exchanges/requested', { params });
export const acceptExchange = (exchangeId) => apiProtected.patch(`/exchanges/${exchangeId}/accept`);
export const declineExchange = (exchangeId, data) => apiProtected.patch(`/exchanges/${exchangeId}/decline`, data);
export const cancelExchange = (exchangeId, data) => apiProtected.patch(`/exchanges/${exchangeId}/cancel`, data);
export const finishExchange = (exchangeId) => apiProtected.patch(`/exchanges/${exchangeId}/finish`);