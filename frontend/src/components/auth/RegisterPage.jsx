import React, { useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { BookOpen, AlertCircle } from 'lucide-react';
import { apiPublic } from '../../api/axios';
import { AuthContext } from '../../App';
const RegisterPage = () => {
  const { register, handleSubmit, formState: { errors } } = useForm();
  const [error, setError] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { login } = useContext(AuthContext); 
  const navigate = useNavigate();

  const onSubmit = async (data) => {
    setIsSubmitting(true);
    setError(null);
    try {
     
      const response = await apiPublic.post('/auth/register', data);
      
      
      const { access_token } = response.data;
      
     
      if (access_token) {
        login(access_token, '/home'); 
      } else {
       
        setError('Регистрация прошла успешно, но не удалось получить токен. Пожалуйста, войдите в систему.');
        navigate('/login');
      }
      
    } catch (err) {
      console.error("Ошибка регистрации:", err);
      if (err.response && err.response.status === 409) {
        setError('Пользователь с таким email уже существует.');
      } else {
        setError('Произошла ошибка при регистрации. Пожалуйста, попробуйте еще раз.');
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  const getInputBorder = (field) => {
    return errors[field] || error ? 'border-red-500' : 'border-[var(--md-sys-color-outline-variant)]';
  };

  return (
    <div className="flex items-center justify-center min-h-screen p-4" style={{ backgroundColor: 'var(--md-sys-color-background)' }}>
      <div className="w-full max-w-md p-8 rounded-xl shadow-lg" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
        <div className="flex items-center justify-center mb-6">
          <BookOpen size={48} style={{ color: 'var(--md-sys-color-primary)' }} className="mr-4" />
          <h1 className="text-3xl font-bold" style={{ color: 'var(--md-sys-color-on-surface)' }}>Регистрация</h1>
        </div>
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
          <div>
            <label className="block text-sm font-medium mb-1" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Имя пользователя</label>
            <input
              type="text"
              {...register("username", { required: "Имя пользователя обязательно" })}
              className={`w-full p-3 rounded-lg border focus:outline-none focus:ring-2 ${getInputBorder('username')}`}
              style={{
                backgroundColor: 'var(--md-sys-color-surface-container-high)',
                color: 'var(--md-sys-color-on-surface)',
                '--tw-ring-color': 'var(--md-sys-color-primary)',
              }}
            />
            {errors.username && (
              <p className="mt-1 text-sm font-medium flex items-center gap-1" style={{ color: 'var(--md-sys-color-error)' }}>
                <AlertCircle size={16} /> {errors.username.message}
              </p>
            )}
          </div>
          <div>
            <label className="block text-sm font-medium mb-1" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Email</label>
            <input
              type="email"
              {...register("email", { required: "Email обязателен" })}
              className={`w-full p-3 rounded-lg border focus:outline-none focus:ring-2 ${getInputBorder('email')}`}
              style={{
                backgroundColor: 'var(--md-sys-color-surface-container-high)',
                color: 'var(--md-sys-color-on-surface)',
                '--tw-ring-color': 'var(--md-sys-color-primary)',
              }}
            />
            {errors.email && (
              <p className="mt-1 text-sm font-medium flex items-center gap-1" style={{ color: 'var(--md-sys-color-error)' }}>
                <AlertCircle size={16} /> {errors.email.message}
              </p>
            )}
            {error && !errors.email && (
              <p className="mt-1 text-sm font-medium flex items-center gap-1" style={{ color: 'var(--md-sys-color-error)' }}>
                <AlertCircle size={16} /> {error}
              </p>
            )}
          </div>
          <div>
            <label className="block text-sm font-medium mb-1" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Пароль</label>
            <input
              type="password"
              {...register("password", { required: "Пароль обязателен", minLength: { value: 8, message: "Пароль должен быть не менее 8 символов" } })}
              className={`w-full p-3 rounded-lg border focus:outline-none focus:ring-2 ${getInputBorder('password')}`}
              style={{
                backgroundColor: 'var(--md-sys-color-surface-container-high)',
                color: 'var(--md-sys-color-on-surface)',
                '--tw-ring-color': 'var(--md-sys-color-primary)',
              }}
            />
            {errors.password && (
              <p className="mt-1 text-sm font-medium flex items-center gap-1" style={{ color: 'var(--md-sys-color-error)' }}>
                <AlertCircle size={16} /> {errors.password.message}
              </p>
            )}
          </div>
          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full py-3 px-4 rounded-lg font-semibold transition-colors duration-200"
            style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}
          >
            {isSubmitting ? 'Регистрация...' : 'Зарегистрироваться'}
          </button>
        </form>
        <p className="mt-6 text-center text-sm" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
          Уже есть аккаунт?{' '}
          <Link to="/login" className="font-semibold" style={{ color: 'var(--md-sys-color-primary)' }}>
            Войти
          </Link>
        </p>
      </div>
    </div>
  );
};

export default RegisterPage;