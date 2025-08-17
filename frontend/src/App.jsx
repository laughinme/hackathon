import React, { useState, useEffect, createContext, useContext, useCallback } from 'react';
import { Routes, Route, useNavigate, useLocation, Outlet, Navigate } from 'react-router-dom';

import apiProtected, { apiPublic, setAccessToken } from './api/axios';
import { getMyProfile } from './api/services';
import { getCookie } from './api/cookies';

import UserHeader from './components/layout/UserHeader';
import LoginPage from './components/auth/LoginPage';
import RegisterPage from './components/auth/RegisterPage';
import Dashboard from './components/pages/Dashboard';
import Moderation from './components/pages/Moderation';
import UsersPage from './components/pages/UsersPage';
import ExchangesPage from './components/pages/ExchangesPage';
import Sidebar from './components/layout/Sidebar';
import UserHeader from './components/layout/UserHeader';
import HomePage from './components/pages/HomePage';
import UserProfilePage from './components/pages/UserProfilePage';
import AddBookPage from './components/pages/AddBookPage';
import BookDetailPage from './components/pages/BookDetailPage';
import OnboardingPage from './components/pages/OnboardingPage';
import MapPage from './components/pages/MapPage';
import LikedBooksPage from './components/pages/LikedBooksPage';
import EditProfilePage from './components/pages/EditProfilePage';
import EditBookPage from './components/pages/EditBookPage';


export const AuthContext = createContext(null);

const StyleInjector = () => {
  React.useEffect(() => {
    const style = document.createElement('style');
    style.textContent = `
      :root {
        --md-sys-color-primary: rgb(219 198 110); --md-sys-color-surface-tint: rgb(219 198 110); --md-sys-color-on-primary: rgb(58 48 0); --md-sys-color-primary-container: rgb(83 70 0); --md-sys-color-on-primary-container: rgb(248 226 135); --md-sys-color-secondary: rgb(209 198 161); --md-sys-color-on-secondary: rgb(54 48 22); --md-sys-color-secondary-container: rgb(78 71 42); --md-sys-color-on-secondary-container: rgb(238 226 188); --md-sys-color-tertiary: rgb(169 208 179); --md-sys-color-on-tertiary: rgb(20 55 35); --md-sys-color-tertiary-container: rgb(44 78 56); --md-sys-color-on-tertiary-container: rgb(197 236 206); --md-sys-color-error: rgb(255 180 171); --md-sys-color-on-error: rgb(105 0 5); --md-sys-color-error-container: rgb(147 0 10); --md-sys-color-on-error-container: rgb(255 218 214); --md-sys-color-background: rgb(21 19 11); --md-sys-color-on-background: rgb(232 226 212); --md-sys-color-surface: rgb(21 19 11); --md-sys-color-on-surface: rgb(232 226 212); --md-sys-color-surface-variant: rgb(75 71 57); --md-sys-color-on-surface-variant: rgb(205 198 180); --md-sys-color-outline: rgb(150 144 128); --md-sys-color-outline-variant: rgb(75 71 57); --md-sys-color-shadow: rgb(0 0 0); --md-sys-color-scrim: rgb(0 0 0); --md-sys-color-inverse-surface: rgb(232 226 212); --md-sys-color-inverse-on-surface: rgb(51 48 39); --md-sys-color-inverse-primary: rgb(109 94 15); --md-sys-color-primary-fixed: rgb(248 226 135); --md-sys-color-on-primary-fixed: rgb(34 27 0); --md-sys-color-primary-fixed-dim: rgb(219 198 110); --md-sys-color-on-primary-fixed-variant: rgb(83 70 0); --md-sys-color-secondary-fixed: rgb(238 226 188); --md-sys-color-on-secondary-fixed: rgb(33 27 4); --md-sys-color-secondary-fixed-dim: rgb(209 198 161); --md-sys-color-on-secondary-fixed-variant: rgb(78 71 42); --md-sys-color-tertiary-fixed: rgb(197 236 206); --md-sys-color-on-tertiary-fixed: rgb(0 33 15); --md-sys-color-tertiary-fixed-dim: rgb(169 208 179); --md-sys-color-on-tertiary-fixed-variant: rgb(44 78 56); --md-sys-color-surface-dim: rgb(21 19 11); --md-sys-color-surface-bright: rgb(60 57 48); --md-sys-color-surface-container-lowest: rgb(16 14 7); --md-sys-color-surface-container-low: rgb(30 27 19); --md-sys-color-surface-container: rgb(34 32 23); --md-sys-color-surface-container-high: rgb(45 42 33); --md-sys-color-surface-container-highest: rgb(56 53 43);
      }
      body { background-color: var(--md-sys-color-background); color: var(--md-sys-color-on-background); font-family: 'Inter', sans-serif; }
      ::-webkit-scrollbar { width: 8px; }
      ::-webkit-scrollbar-track { background: var(--md-sys-color-surface-container-low); }
      ::-webkit-scrollbar-thumb { background-color: var(--md-sys-color-surface-variant); border-radius: 10px; border: 2px solid var(--md-sys-color-surface-container-low); }
      ::-webkit-scrollbar-thumb:hover { background-color: var(--md-sys-color-primary); }
    `;
    document.head.appendChild(style);
    return () => {
      document.head.removeChild(style);
    };
  }, []);
  return null;
};

const PrivateRoute = () => {
  const { token, isAuthLoading } = useContext(AuthContext);

  if (isAuthLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <p style={{ color: 'var(--md-sys-color-on-background)' }}>Загрузка...</p>
      </div>
    );
  }

  return token ? <Outlet /> : <Navigate to="/login" replace />;
};

const AppContent = () => {
    const { token } = useContext(AuthContext);
    const location = useLocation();
    
    const noHeaderPaths = ['/login', '/register', '/onboarding', '/map'];
    const showHeader = token && !noHeaderPaths.includes(location.pathname);

    return (
        <div className="flex flex-col min-h-screen">
            {showHeader && <UserHeader />}
            <main className="flex-1">
                <Routes>
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                    
                    <Route element={<PrivateRoute />}>
                        <Route path="/onboarding" element={<OnboardingPage />} />
                        <Route path="/" element={<HomePage />} />
                        <Route path="/home" element={<HomePage />} />
                        <Route path="/profile" element={<UserProfilePage />} />
                        <Route path="/profile/edit" element={<EditProfilePage />} />
                        <Route path="/add-book" element={<AddBookPage />} />
                        <Route path="/book/:bookId" element={<BookDetailPage />} />
                        <Route path="/book/:bookId/edit" element={<EditBookPage />} />
                        <Route path="/exchanges" element={<ExchangesPage />} />
                        <Route path="/map" element={<MapPage />} />
                        <Route path="/liked-books" element={<LikedBooksPage />} />
                    </Route>
                </Routes>
            </main>
        </div>
    );
}

export default function App() {
  const [token, setToken] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);
  const [isAuthLoading, setIsAuthLoading] = useState(true);
  const navigate = useNavigate();

  const logout = useCallback(() => {
    apiProtected.post('/auth/logout').catch(err => console.error("Ошибка при выходе из системы, но продолжение...", err));
    setAccessToken(null);
    setToken(null);
    setCurrentUser(null);
    document.cookie = "refresh_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "fastapi-csrf-token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    navigate('/login');
  }, [navigate]);
  
  useEffect(() => {
    const bootstrapAuth = async () => {
      try {
        const csrfToken = getCookie('fastapi-csrf-token');
        if (!csrfToken) {
          throw new Error("CSRF token не найден. Считаем, что пользователь не вошел в систему.");
        }
        
        const response = await apiPublic.post('/auth/refresh', {}, {
          headers: { 'x-csrf-token': csrfToken },
          withCredentials: true,
        });

        const newAccessToken = response.data.access_token;
        setAccessToken(newAccessToken);

        const { data: user } = await getMyProfile();
        
        setToken(newAccessToken);
        setCurrentUser(user);

      } catch (error) {
        setToken(null);
        setCurrentUser(null);
      } finally {
        setIsAuthLoading(false);
      }
    };

    bootstrapAuth();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const login = async (newToken, redirectPath = null) => {
    setAccessToken(newToken);
    try {
      const { data: user } = await getMyProfile();
      setToken(newToken);
      setCurrentUser(user);
      if (user) {
        navigate(redirectPath || (user.is_onboarded ? '/home' : '/onboarding'));
      }
    } catch (error) {
       console.error("Ошибка при входе: не удалось загрузить профиль", error);
       logout();
    }
  };
  
  const fetchUserProfile = useCallback(async () => {
    try {
        const { data } = await getMyProfile();
        setCurrentUser(data);
        return data;
    } catch (error) {
        console.error("Ошибка при обновлении профиля, выход из системы.", error);
        logout();
        return null;
    }
  }, [logout]);

  return (
    <AuthContext.Provider value={{ token, currentUser, login, logout, fetchUserProfile, isAuthLoading }}>
      <StyleInjector />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        
        <Route path="/onboarding" element={<PrivateRoute><OnboardingPage /></PrivateRoute>} />
        
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
            <Route path="books" element={
              <div className="flex flex-row min-h-screen">
                <Sidebar />
                <main className="flex flex-col flex-1 p-8 overflow-auto">
                  <Moderation />
                </main>
              </div>
            } />
            <Route path="users" element={
              <div className="flex flex-row min-h-screen">
                <Sidebar />
                <main className="flex flex-col flex-1 p-8 overflow-auto">
                  <UsersPage />
                </main>
              </div>
            } />
            <Route path="exchanges" element={
              <div className="flex flex-row min-h-screen">
                <Sidebar />
                <main className="flex flex-col flex-1 p-8 overflow-auto">
                  <ExchangesPage />
                </main>
              </div>
            } />
        </Route>
        
        <Route path="/home" element={<PrivateRoute><div className="flex flex-col min-h-screen"><UserHeader /><main className="flex-1 overflow-auto"><HomePage books={allBooks} /></main></div></PrivateRoute>} />
        <Route path="/profile" element={<PrivateRoute><div className="flex flex-col min-h-screen"><UserHeader /><main className="flex-1 overflow-auto"><UserProfilePage allBooks={allBooks} /></main></div></PrivateRoute>} />
        <Route path="/add-book" element={<PrivateRoute><div className="flex flex-col min-h-screen"><UserHeader /><main className="flex-1 overflow-auto"><AddBookPage onAddBook={addBook} /></main></div></PrivateRoute>} />
        
        {/* --- ИЗМЕНЕНИЕ ЗДЕСЬ --- */}
        <Route path="/map" element={
          <PrivateRoute>
            <div className="flex flex-col min-h-screen">
              <UserHeader />
              <main className="flex-1 overflow-auto relative">
                <MapPage />
              </main>
            </div>
          </PrivateRoute>} 
        />
        
      </Routes>
    </AuthContext.Provider>
  );
}