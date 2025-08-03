import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { Plus, User as UserIcon, BookOpen } from 'lucide-react';
import { AuthContext } from '../../App';
import apiProtected from '../../api/axios';
export default function OnboardingPage() {
  const { register, handleSubmit, watch, setValue, formState: { errors } } = useForm();
  const { token } = useContext(AuthContext);
  const navigate = useNavigate();
  const [genres, setGenres] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedGenres, setSelectedGenres] = useState(new Set());
  const avatarFile = watch('avatar');
  const [avatarPreview, setAvatarPreview] = useState(null);
  useEffect(() => {
    const fetchGenres = async () => {
      try {
        const response = await apiProtected.get('/books/genres/');
        setGenres(response.data);
      } catch (err) {
        console.error("Failed to fetch genres:", err);
        setError("Не удалось загрузить список жанров.");
      } finally {
        setLoading(false);
      }
    };
    fetchGenres();
  }, [token]);
  useEffect(() => {
    if (avatarFile && avatarFile.length > 0) {
      const file = avatarFile[0];
      setAvatarPreview(URL.createObjectURL(file));
      return () => URL.revokeObjectURL(file);
    }
    setAvatarPreview(null);
  }, [avatarFile]);
  const handleGenreToggle = (genreId) => {
    const newSelectedGenres = new Set(selectedGenres);
    if (newSelectedGenres.has(genreId)) {
      newSelectedGenres.delete(genreId);
    } else {
      newSelectedGenres.add(genreId);
    }
    setSelectedGenres(newSelectedGenres);
    setValue('favorite_genres', Array.from(newSelectedGenres), { shouldValidate: true });
  };
  const onSubmit = async (data) => {
    setLoading(true);
    setError(null);
    try {
      await apiProtected.patch('/users/me/', {
        bio: data.bio,
      });
      await apiProtected.put('/users/me/genres', {
        favorite_genres: Array.from(selectedGenres),
      });
      if (data.avatar && data.avatar.length > 0) {
        const formData = new FormData();
        formData.append('file', data.avatar[0]);
        await apiProtected.put('/users/me/picture', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
      }
      navigate('/home');
    } catch (err) {
      console.error("Onboarding failed:", err);
      setError("Не удалось сохранить данные. Попробуйте еще раз.");
    } finally {
      setLoading(false);
    }
  };
  if (loading && !genres.length) {
    return <div className="p-8 text-center" style={{ color: 'var(--md-sys-color-on-background)' }}>Загрузка...</div>;
  }
  if (error) {
    return <div className="p-8 text-center text-red-400" style={{ color: 'var(--md-sys-color-error)' }}>{error}</div>;
  }
  return (
    <div className="flex items-center justify-center min-h-screen p-4" style={{ backgroundColor: 'var(--md-sys-color-background)' }}>
      <div className="w-full max-w-2xl p-8 rounded-xl shadow-lg" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
        <div className="flex items-center justify-center mb-6">
          <BookOpen size={48} style={{ color: 'var(--md-sys-color-primary)' }} className="mr-4" />
          <h1 className="text-3xl font-bold" style={{ color: 'var(--md-sys-color-on-surface)' }}>Добро пожаловать!</h1>
        </div>
        <p className="text-center mb-8" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
          Пожалуйста, расскажите немного о себе, чтобы мы могли подобрать для вас книги.
        </p>
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
          <div className="flex flex-col items-center gap-4">
            <div className="relative h-32 w-32 rounded-full overflow-hidden border-2" style={{ borderColor: 'var(--md-sys-color-outline-variant)' }}>
              {avatarPreview ? (
                <img src={avatarPreview} alt="Avatar Preview" className="h-full w-full object-cover" />
              ) : (
                <div className="h-full w-full flex items-center justify-center" style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)', color: 'var(--md-sys-color-on-surface-variant)' }}>
                  <UserIcon size={48} />
                </div>
              )}
              <label htmlFor="avatar-upload" className="absolute bottom-0 right-0 p-2 rounded-full cursor-pointer" style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}>
                <Plus size={20} />
                <input
                  id="avatar-upload"
                  type="file"
                  accept="image/jpeg,image/png"
                  {...register("avatar")}
                  className="hidden"
                />
              </label>
            </div>
            {errors.avatar && <p className="mt-1 text-sm text-red-500" style={{ color: 'var(--md-sys-color-error)' }}>{errors.avatar.message}</p>}
          </div>
          <div>
            <label className="block text-sm font-medium mb-1" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
              О себе
            </label>
            <textarea
              {...register("bio", { maxLength: { value: 250, message: "Не более 250 символов" } })}
              rows="4"
              placeholder="Расскажите о своих книжных предпочтениях, любимых авторах..."
              className="w-full p-3 rounded-lg border focus:outline-none focus:ring-2"
              style={{
                backgroundColor: 'var(--md-sys-color-surface-container-high)',
                borderColor: 'var(--md-sys-color-outline-variant)',
                color: 'var(--md-sys-color-on-surface)',
                '--tw-ring-color': 'var(--md-sys-color-primary)',
              }}
            />
            {errors.bio && <p className="mt-1 text-sm text-red-500" style={{ color: 'var(--md-sys-color-error)' }}>{errors.bio.message}</p>}
          </div>
          <div>
            <label className="block text-sm font-medium mb-2" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
              Любимые жанры (выберите хотя бы один)
            </label>
            <div className="flex flex-wrap gap-2">
              {genres.map((genre) => (
                <button
                  key={genre.id}
                  type="button"
                  onClick={() => handleGenreToggle(genre.id)}
                  className={`py-2 px-4 rounded-full text-sm font-medium transition-colors duration-200 ${selectedGenres.has(genre.id) ? 'bg-[var(--md-sys-color-primary)] text-[var(--md-sys-color-on-primary)]' : 'bg-[var(--md-sys-color-surface-container-high)] text-[var(--md-sys-color-on-surface-variant)]'}`}
                >
                  {genre.name}
                </button>
              ))}
            </div>
            <input type="hidden" {...register("favorite_genres", { validate: value => value.length > 0 || "Выберите хотя бы один жанр" })} />
            {errors.favorite_genres && <p className="mt-2 text-sm text-red-500" style={{ color: 'var(--md-sys-color-error)' }}>{errors.favorite_genres.message}</p>}
          </div>
          <button
            type="submit"
            disabled={loading}
            className="w-full py-3 px-4 rounded-lg font-semibold transition-colors duration-200"
            style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}
          >
            {loading ? 'Сохранение...' : 'Завершить'}
          </button>
        </form>
      </div>
    </div>
  );
}