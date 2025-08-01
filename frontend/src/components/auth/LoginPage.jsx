import React, { useState, useContext } from 'react';
import { Link } from 'react-router-dom';
import { apiPublic } from '../../api/axios';
import { AuthContext } from '../../App';
import { BookOpen } from 'lucide-react';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useContext(AuthContext);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await apiPublic.post('/auth/login', {
        email,
        password,
      });

      const { access_token } = response.data;
      if (access_token) {
        login(access_token);
      } else {
        setError('Login successful but no token received.');
      }
    } catch (err) {
      console.error(err);
      if (err.response && err.response.status === 401) {
        setError('Неправильный email или пароль.');
      } else {
        setError('Произошла ошибка при входе. Попробуйте еще раз.');
      }
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen p-4" style={{ backgroundColor: 'var(--md-sys-color-background)' }}>
      <div className="w-full max-w-md p-8 rounded-xl shadow-lg" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
        <div className="flex items-center justify-center mb-6">
          <BookOpen size={48} style={{ color: 'var(--md-sys-color-primary)' }} className="mr-4" />
          <h1 className="text-3xl font-bold" style={{ color: 'var(--md-sys-color-on-surface)' }}>Войти</h1>
        </div>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium mb-1" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full p-3 rounded-lg border focus:outline-none focus:ring-2"
              style={{
                backgroundColor: 'var(--md-sys-color-surface-container-high)',
                borderColor: 'var(--md-sys-color-outline-variant)',
                color: 'var(--md-sys-color-on-surface)',
                '--tw-ring-color': 'var(--md-sys-color-primary)',
              }}
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Пароль</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-3 rounded-lg border focus:outline-none focus:ring-2"
              style={{
                backgroundColor: 'var(--md-sys-color-surface-container-high)',
                borderColor: 'var(--md-sys-color-outline-variant)',
                color: 'var(--md-sys-color-on-surface)',
                '--tw-ring-color': 'var(--md-sys-color-primary)',
              }}
              required
            />
          </div>
          {error && <p className="text-sm font-medium" style={{ color: 'var(--md-sys-color-error)' }}>{error}</p>}
          <button
            type="submit"
            className="w-full py-3 px-4 rounded-lg font-semibold transition-colors duration-200"
            style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}
          >
            Войти
          </button>
        </form>
        <p className="mt-6 text-center text-sm" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
          Ещё нет аккаунта?{' '}
          <Link to="/register" className="font-semibold" style={{ color: 'var(--md-sys-color-primary)' }}>
            Зарегистрироваться
          </Link>
        </p>
      </div>
    </div>
  );
};

export default LoginPage;