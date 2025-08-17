import apiProtected from './axios';

// Admin API helpers

// Books moderation
export const adminListBooks = async ({ status = 'pending', limit = 50 } = {}) => {
  const { data } = await apiProtected.get(`/admins/books/`, {
    params: { status, limit },
  });
  return data;
};

export const adminAcceptBook = async (bookId) => {
  const { data } = await apiProtected.post(`/admins/books/${bookId}/accept`);
  return data;
};

export const adminRejectBook = async (bookId, reason) => {
  const { data } = await apiProtected.post(`/admins/books/${bookId}/reject`, { reason });
  return data;
};

// Users
export const adminListUsers = async (params = {}) => {
  const { data } = await apiProtected.get(`/admins/users/`, { params });
  return data; // CursorPage { items, next_cursor }
};

export const adminSetUserBan = async (userId, banned) => {
  const { data } = await apiProtected.post(`/admins/users/${userId}/ban`, { banned });
  return data;
};

// Stats
export const adminStatsActiveUsers = async (days = 30) => {
  const { data } = await apiProtected.get(`/admins/stats/active-users`, { params: { days } });
  return data;
};

export const adminStatsRegistrations = async (days = 30) => {
  const { data } = await apiProtected.get(`/admins/stats/registrations`, { params: { days } });
  return data;
};

export const adminStatsBooks = async (days = 30) => {
  const { data } = await apiProtected.get(`/admins/stats/books/stats`, { params: { days } });
  return data;
};

export const adminStatsBookById = async (bookId, days = 30) => {
  const { data } = await apiProtected.get(`/admins/stats/books/${bookId}/stats`, { params: { days } });
  return data;
};

// Exchanges
export const adminListExchanges = async (params = {}) => {
  const { data } = await apiProtected.get(`/admins/exchanges/`, { params });
  return data; // CursorPage
};

export const adminGetExchange = async (exchangeId) => {
  const { data } = await apiProtected.get(`/admins/exchanges/${exchangeId}/`);
  return data;
};

export const adminForceFinishExchange = async (exchangeId) => {
  const { data } = await apiProtected.post(`/admins/exchanges/${exchangeId}/force-finish`);
  return data;
};

export const adminForceCancelExchange = async (exchangeId) => {
  const { data } = await apiProtected.post(`/admins/exchanges/${exchangeId}/force-cancel`);
  return data;
};

export default {
  adminListBooks,
  adminAcceptBook,
  adminRejectBook,
  adminListUsers,
  adminSetUserBan,
  adminStatsActiveUsers,
  adminStatsRegistrations,
  adminStatsBooks,
  adminStatsBookById,
  adminListExchanges,
  adminGetExchange,
  adminForceFinishExchange,
  adminForceCancelExchange,
};


