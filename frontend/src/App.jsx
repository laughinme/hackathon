import React, { useState, useEffect, createContext } from 'react';
import { Routes, Route, useNavigate, useLocation } from 'react-router-dom';
import LoginPage from './components/auth/LoginPage';
import RegisterPage from './components/auth/RegisterPage';
import Dashboard from './components/pages/Dashboard';
import Moderation from './components/pages/Moderation';
import Sidebar from './components/layout/Sidebar';
import UserHeader from './components/layout/UserHeader';
import HomePage from './components/pages/HomePage';
import UserProfilePage from './components/pages/UserProfilePage';
import AddBookPage from './components/pages/AddBookPage';
import OnboardingPage from './components/pages/OnboardingPage';
import PrivateRoute from './components/layout/PrivateRoute';
import apiProtected, { apiPublic, setAccessToken, getAccessToken } from './api/axios';
export const AuthContext = createContext(null);
const INITIAL_BOOKS = [
    { id: 1, title: 'Война и мир', author: 'Лев Толстой', rating: 4.8, genre: 'Классика', distanceNum: 0.8, distance: '0.8 км', owner: { name: 'Анна К.', rating: 4.9, avatar: 'https://placehold.co/80x80/DBC66E/3A3000?text=A' }, condition: 'good', added: '2 ч. назад', status: 'available', image: 'https://placehold.co/400x600/3A342B/E8E2D4?text=Война+и+мир', tags: ['Классика', 'История'], publisher: 'Эксмо', year: 2019, description: 'Величайший роман всех времен и народов, эпическая картина русской жизни начала XIX века.' },
    { id: 2, title: 'Гарри Поттер и философский камень', author: 'Дж. К. Роулинг', rating: 4.9, genre: 'Фэнтези', distanceNum: 1.2, distance: '1.2 км', owner: { name: 'Михаил П.', rating: 4.7, avatar: null }, condition: 'new', added: '2 ч. назад', status: 'available', image: 'https://placehold.co/400x600/3A342B/E8E2D4?text=Гарри+Поттер', tags: ['Фэнтези', 'Приключения'], publisher: 'Росмэн', year: 2000, description: 'Приключения юного волшебника Гарри Поттера и его друзей в школе чародейства и волшебства Хогвартс.' },
    { id: 3, title: 'Мастер и Маргарита', author: 'Михаил Булгаков', rating: 4.7, genre: 'Классика', distanceNum: 2.5, distance: '2.5 км', owner: { name: 'Елена С.', rating: 4.8, avatar: null }, condition: 'good', added: '2 ч. назад', status: 'reserved', image: 'https://placehold.co/400x600/3A342B/E8E2D4?text=Мастер+и+Маргарита', tags: ['Мистика', 'Сатира'], publisher: 'АСТ', year: 2015, description: 'Захватывающая история о визите дьявола в Москву 1930-х годов, переплетенная с историей Понтия Пилата.' },
    { id: 4, title: 'Атлант расправил плечи', author: 'Айн Рэнд', rating: 4.6, genre: 'Философия', distanceNum: 3.1, distance: '3.1 км', owner: { name: 'Дмитрий В.', rating: 5.0, avatar: null }, condition: 'good', added: '5 ч. назад', status: 'available', image: 'https://placehold.co/400x600/3A342B/E8E2D4?text=Атлант', tags: ['Философия', 'Антиутопия'], publisher: 'Альпина', year: 2021, description: 'Роман-антиутопия, в котором ключевые фигуры американского бизнеса объявляют забастовку и исчезают, оставляя страну в хаосе.' },
];
const StyleInjector = () => {
  React.useEffect(() => {
    const style = document.createElement('style');
    style.textContent = `
      :root {
        --md-sys-color-primary: rgb(219 198 110);
        --md-sys-color-surface-tint: rgb(219 198 110);
        --md-sys-color-on-primary: rgb(58 48 0);
        --md-sys-color-primary-container: rgb(83 70 0);
        --md-sys-color-on-primary-container: rgb(248 226 135);
        --md-sys-color-secondary: rgb(209 198 161);
        --md-sys-color-on-secondary: rgb(54 48 22);
        --md-sys-color-secondary-container: rgb(78 71 42);
        --md-sys-color-on-secondary-container: rgb(238 226 188);
        --md-sys-color-tertiary: rgb(169 208 179);
        --md-sys-color-on-tertiary: rgb(20 55 35);
        --md-sys-color-tertiary-container: rgb(44 78 56);
        --md-sys-color-on-tertiary-container: rgb(197 236 206);
        --md-sys-color-error: rgb(255 180 171);
        --md-sys-color-on-error: rgb(105 0 5);
        --md-sys-color-error-container: rgb(147 0 10);
        --md-sys-color-on-error-container: rgb(255 218 214);
        --md-sys-color-background: rgb(21 19 11);
        --md-sys-color-on-background: rgb(232 226 212);
        --md-sys-color-surface: rgb(21 19 11);
        --md-sys-color-on-surface: rgb(232 226 212);
        --md-sys-color-surface-variant: rgb(75 71 57);
        --md-sys-color-on-surface-variant: rgb(205 198 180);
        --md-sys-color-outline: rgb(150 144 128);
        --md-sys-color-outline-variant: rgb(75 71 57);
        --md-sys-color-shadow: rgb(0 0 0);
        --md-sys-color-scrim: rgb(0 0 0);
        --md-sys-color-inverse-surface: rgb(232 226 212);
        --md-sys-color-inverse-on-surface: rgb(51 48 39);
        --md-sys-color-inverse-primary: rgb(109 94 15);
        --md-sys-color-primary-fixed: rgb(248 226 135);
        --md-sys-color-on-primary-fixed: rgb(34 27 0);
        --md-sys-color-primary-fixed-dim: rgb(219 198 110);
        --md-sys-color-on-primary-fixed-variant: rgb(83 70 0);
        --md-sys-color-secondary-fixed: rgb(238 226 188);
        --md-sys-color-on-secondary-fixed: rgb(33 27 4);
        --md-sys-color-secondary-fixed-dim: rgb(209 198 161);
        --md-sys-color-on-secondary-fixed-variant: rgb(78 71 42);
        --md-sys-color-tertiary-fixed: rgb(197 236 206);
        --md-sys-color-on-tertiary-fixed: rgb(0 33 15);
        --md-sys-color-tertiary-fixed-dim: rgb(169 208 179);
        --md-sys-color-on-tertiary-fixed-variant: rgb(44 78 56);
        --md-sys-color-surface-dim: rgb(21 19 11);
        --md-sys-color-surface-bright: rgb(60 57 48);
        --md-sys-color-surface-container-lowest: rgb(16 14 7);
        --md-sys-color-surface-container-low: rgb(30 27 19);
        --md-sys-color-surface-container: rgb(34 32 23);
        --md-sys-color-surface-container-high: rgb(45 42 33);
        --md-sys-color-surface-container-highest: rgb(56 53 43);
      }
      body {
        background-color: var(--md-sys-color-background);
        color: var(--md-sys-color-on-background);
        font-family: 'Inter', sans-serif;
      }
      ::-webkit-scrollbar {
        width: 8px;
      }
      ::-webkit-scrollbar-track {
        background: var(--md-sys-color-surface-container-low);
      }
      ::-webkit-scrollbar-thumb {
        background-color: var(--md-sys-color-surface-variant);
        border-radius: 10px;
        border: 2px solid var(--md-sys-color-surface-container-low);
      }
      ::-webkit-scrollbar-thumb:hover {
        background-color: var(--md-sys-color-primary);
      }
    `;
    document.head.appendChild(style);
    return () => {
      document.head.removeChild(style);
    };
  }, []);
  return null;
};
export default function App() {
  const [token, setToken] = useState(getAccessToken() || null);
  const [isAuthLoading, setIsAuthLoading] = useState(true);
  const navigate = useNavigate();
  const location = useLocation();
  const [allBooks, setAllBooks] = useState(INITIAL_BOOKS);
  const addBook = (formData) => {
    const newBook = {
      id: Date.now(),
      title: formData.title,
      author: formData.author,
      genre: formData.genre,
      condition: formData.condition,
      description: formData.description,
      year: formData.year,
      publisher: formData.publisher,
      tags: formData.tags,
      image: formData.images.length > 0 ? URL.createObjectURL(formData.images[0]) : `https://placehold.co/400x600/3A342B/E8E2D4?text=${formData.title.replace(' ', '+')}`,
      rating: 0,
      distanceNum: 0.1,
      distance: '0.1 км',
      owner: { name: 'Анна К.' },
      added: 'только что',
      status: 'available',
      location: formData.exchangeLocation,
    };
    setAllBooks(prevBooks => [newBook, ...prevBooks]);
  };
  useEffect(() => {
    const checkAuthStatus = async () => {
      const currentToken = getAccessToken();
      if (currentToken) {
        setToken(currentToken);
        setIsAuthLoading(false);
        return;
      }
      try {
        const response = await apiPublic.post('/auth/refresh');
        const newAccessToken = response.data.access_token;
        setToken(newAccessToken);
        setAccessToken(newAccessToken);
        if (location.pathname === '/login' || location.pathname === '/register') {
            navigate('/home', { replace: true });
        }
      } catch (error) {
        setToken(null);
        setAccessToken(null);
        if (location.pathname !== '/login' && location.pathname !== '/register') {
          navigate('/login', { replace: true });
        }
      } finally {
        setIsAuthLoading(false);
      }
    };
    checkAuthStatus();
  }, [location.pathname, navigate]);
  const login = (newToken, redirectPath = '/home') => {
    setToken(newToken);
    setAccessToken(newToken);
    navigate(redirectPath);
  };
  const logout = () => {
    apiProtected.post('/auth/logout').finally(() => {
      setToken(null);
      setAccessToken(null);
      navigate('/login');
    });
  };
  if (isAuthLoading) {
    return (
        <div className="flex items-center justify-center min-h-screen" style={{ backgroundColor: 'var(--md-sys-color-background)' }}>
          <p style={{ color: 'var(--md-sys-color-on-background)' }}>Загрузка...</p>
        </div>
    );
  }
  return (
    <AuthContext.Provider value={{ token, login, logout }}>
      <StyleInjector />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/onboarding" element={<PrivateRoute />}>
          <Route index element={<OnboardingPage />} />
        </Route>
        <Route path="/" element={<PrivateRoute />}>
          <Route index element={
            <div className="flex flex-row min-h-screen">
              <Sidebar />
              <main className="flex flex-col flex-1 p-8 overflow-auto">
                <Dashboard />
              </main>
            </div>
          } />
          <Route path="moderation" element={
            <div className="flex flex-row min-h-screen">
              <Sidebar />
              <main className="flex flex-col flex-1 p-8 overflow-auto">
                <Moderation />
              </main>
            </div>
          } />
        </Route>
        <Route path="/home" element={<PrivateRoute />}>
          <Route index element={
            <div className="flex flex-col min-h-screen">
              <UserHeader />
              <main className="flex-1 overflow-auto">
                <HomePage books={allBooks} />
              </main>
            </div>
          } />
        </Route>
        <Route path="/profile" element={<PrivateRoute />}>
          <Route index element={
            <div className="flex flex-col min-h-screen">
              <UserHeader />
              <main className="flex-1 overflow-auto">
                <UserProfilePage allBooks={allBooks} />
              </main>
            </div>
          } />
        </Route>
        <Route path="/add-book" element={<PrivateRoute />}>
          <Route index element={
            <div className="flex flex-col min-h-screen">
              <UserHeader />
              <main className="flex-1 overflow-auto">
                <AddBookPage onAddBook={addBook} />
              </main>
            </div>
          } />
        </Route>
      </Routes>
    </AuthContext.Provider>
  );
}