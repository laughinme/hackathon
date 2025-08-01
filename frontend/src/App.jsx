import React, { useState, useEffect, createContext, useContext } from 'react';
import { Routes, Route, useNavigate, useLocation } from 'react-router-dom';
import { BookOpen, BarChart3, ShieldCheck, Users, Clock, CheckCircle, XCircle, LayoutDashboard, Library } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

import LoginPage from './components/auth/LoginPage';
import RegisterPage from './components/auth/RegisterPage';
import Dashboard from './components/pages/Dashboard';
import Moderation from './components/pages/Moderation';
import Sidebar from './components/layout/Sidebar';
import PrivateRoute from './components/layout/PrivateRoute';

import apiProtected, { setAccessToken, getAccessToken } from './api/axios';

export const AuthContext = createContext(null);

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
  const navigate = useNavigate();

  useEffect(() => {
    if (token) {
      setAccessToken(token);
    } else {
      setAccessToken(null);
    }
  }, [token]);

  const login = (newToken) => {
    setToken(newToken);
    setAccessToken(newToken);
    navigate('/');
  };

  const logout = () => {
    setToken(null);
    setAccessToken(null);
    // TODO: можно добавить запрос на /api/v1/auth/logout, если нужно
    navigate('/login');
  };

  return (
    <AuthContext.Provider value={{ token, login, logout }}>
      <StyleInjector />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        
        <Route element={<PrivateRoute />}>
          <Route path="/" element={
            <div className="flex flex-row min-h-screen">
              <Sidebar />
              <main className="flex flex-col flex-1 p-8 overflow-auto">
                <Dashboard />
              </main>
            </div>
          } />
          <Route path="/moderation" element={
            <div className="flex flex-row min-h-screen">
              <Sidebar />
              <main className="flex flex-col flex-1 p-8 overflow-auto">
                <Moderation />
              </main>
            </div>
          } />
        </Route>
      </Routes>
    </AuthContext.Provider>
  );
}