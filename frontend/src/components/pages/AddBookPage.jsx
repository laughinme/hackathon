import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { ArrowLeft, Upload, Book, Camera, X } from 'lucide-react';
import apiProtected from '../../api/axios';

export default function AddBookPage({ onAddBook }) {
  const navigate = useNavigate();

  const [genres, setGenres] = useState([]);
  const [authors, setAuthors] = useState([]);
  const [exchangeLocations, setExchangeLocations] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  const [selectedImages, setSelectedImages] = useState([]);
  const [imagePreviewUrls, setImagePreviewUrls] = useState([]);

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors, isSubmitting }
  } = useForm({
    defaultValues: {
      language: 'ru',
      condition: 'good',
    }
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [genresRes, authorsRes, locationsRes] = await Promise.all([
          apiProtected.get('/books/genres/'),
          apiProtected.get('/books/authors/'),
          apiProtected.get('/geo/exchange_locations/')
        ]);
        setGenres(genresRes.data);
        setAuthors(authorsRes.data);
        setExchangeLocations(locationsRes.data);
      } catch (error) {
        console.error("Failed to fetch form data", error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleImageUpload = (event) => {
    const files = Array.from(event.target.files || []);
    if (files.length === 0) return;

    const newFiles = [...selectedImages, ...files].slice(0, 3);
    setSelectedImages(newFiles);

    const newUrls = newFiles.map(file => URL.createObjectURL(file));
    setImagePreviewUrls(newUrls);

    setValue('images', newFiles, { shouldValidate: true });
  };

  const removeImage = (index) => {
    const newFiles = selectedImages.filter((_, i) => i !== index);
    const newUrls = imagePreviewUrls.filter((_, i) => i !== index);
    
    setSelectedImages(newFiles);
    setImagePreviewUrls(newUrls);
    setValue('images', newFiles);
  };

  const handleFormSubmit = async (data) => {
    const payload = {
      title: data.title,
      description: data.description,
      author_id: parseInt(data.author_id, 10),
      genre_id: parseInt(data.genre_id, 10),
      language: data.language,
      condition: data.condition,
      exchange_location_id: parseInt(data.exchange_location_id, 10),
    };
    
    const success = await onAddBook(payload, selectedImages);

    if (success) {
      alert('Книга успешно добавлена!');
      navigate('/home');
    } else {
      alert('Произошла ошибка при добавлении книги.');
    }
  };

  const FormInput = ({ id, label, required, placeholder, error, registerProps, ...props }) => (
    <div className="space-y-1">
      <label htmlFor={id} className="text-sm font-medium">{label} {required && '*'}</label>
      <input
        id={id}
        placeholder={placeholder}
        className={`w-full p-2 rounded-lg border bg-transparent focus:outline-none focus:ring-2 ${error ? 'border-red-500' : 'border-[var(--md-sys-color-outline)] focus:border-[var(--md-sys-color-primary)]'} `}
        style={{'--tw-ring-color': 'var(--md-sys-color-primary)'}}
        {...registerProps}
        {...props}
      />
      {error && <p className="text-sm text-red-400">{error.message}</p>}
    </div>
  );
  
  const FormTextarea = ({ id, label, error, registerProps, ...props }) => (
    <div className="space-y-1">
      <label htmlFor={id} className="text-sm font-medium">{label}</label>
      <textarea
        id={id}
        className="w-full p-2 rounded-lg border bg-transparent focus:outline-none focus:ring-2 border-[var(--md-sys-color-outline)] focus:border-[var(--md-sys-color-primary)]"
        style={{'--tw-ring-color': 'var(--md-sys-color-primary)'}}
        rows={4}
        {...registerProps}
        {...props}
      ></textarea>
      {error && <p className="text-sm text-red-400">{error.message}</p>}
    </div>
  );

  const FormSelect = ({ id, label, required, error, registerProps, children }) => (
    <div className="space-y-1">
       <label htmlFor={id} className="text-sm font-medium">{label} {required && '*'}</label>
       <select
         id={id}
         className={`w-full p-2 rounded-lg border bg-transparent focus:outline-none focus:ring-2 ${error ? 'border-red-500' : 'border-[var(--md-sys-color-outline)] focus:border-[var(--md-sys-color-primary)]'}`}
         style={{'--tw-ring-color': 'var(--md-sys-color-primary)'}}
         {...registerProps}
       >
        {children}
       </select>
       {error && <p className="text-sm text-red-400">{error.message}</p>}
    </div>
  );

  if (isLoading) {
    return <div className="p-8 text-center">Загрузка данных для формы...</div>;
  }

  return (
    <div className="p-8">
      <div className="flex items-center gap-4 mb-6">
        <button onClick={() => navigate(-1)} className="flex items-center gap-2 font-semibold" style={{ color: 'var(--md-sys-color-primary)' }}>
          <ArrowLeft className="h-5 w-5" />
          Назад
        </button>
        <div className="h-6 w-px" style={{backgroundColor: 'var(--md-sys-color-outline-variant)'}}></div>
        <div className="flex items-center gap-2">
          <Book className="h-5 w-5" style={{ color: 'var(--md-sys-color-primary)' }}/>
          <h1 className="text-xl font-bold">Добавить книгу</h1>
        </div>
      </div>
      
      <div className="max-w-4xl mx-auto">
        <form onSubmit={handleSubmit(handleFormSubmit)} className="space-y-8">
          <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <h3 className="text-lg font-semibold mb-6">Основная информация</h3>
            <div className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <FormInput id="title" label="Название книги" required placeholder="Введите название" error={errors.title} registerProps={register('title', { required: 'Название книги обязательно' })} />
                
                <FormSelect id="author" label="Автор" required error={errors.author_id} registerProps={register('author_id', { required: 'Автор обязателен' })}>
                    <option value="">Выберите автора</option>
                    {authors.map(a => <option key={a.id} value={a.id}>{a.name}</option>)}
                </FormSelect>

                <FormSelect id="genre" label="Жанр" required error={errors.genre_id} registerProps={register('genre_id', { required: 'Жанр обязателен' })}>
                    <option value="">Выберите жанр</option>
                    {genres.map(g => <option key={g.id} value={g.id}>{g.name}</option>)}
                </FormSelect>

                <div className="space-y-1">
                   <label className="text-sm font-medium">Состояние книги *</label>
                   <div className="flex gap-6 pt-2">
                     {['new', 'good', 'normal'].map(condition => (
                       <div key={condition} className="flex items-center space-x-2">
                         <input type="radio" id={condition} value={condition} {...register('condition')} className="w-4 h-4" defaultChecked={condition === 'good'} />
                         <label htmlFor={condition}>{condition === 'new' ? 'Новая' : condition === 'good' ? 'Хорошее' : 'Нормальное'}</label>
                       </div>
                     ))}
                   </div>
                </div>
              </div>
              <FormTextarea id="description" label="Описание" placeholder="Опишите книгу, её состояние, особенности..." registerProps={register('description')} />
            </div>
          </div>
          
          <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <h3 className="text-lg font-semibold mb-4 flex items-center gap-2"><Camera /> Фотографии книги</h3>
            <div className="grid grid-cols-2 sm:grid-cols-3 gap-4">
               {imagePreviewUrls.map((url, index) => (
                 <div key={url} className="relative aspect-[3/4] rounded-lg overflow-hidden">
                   <img src={url} alt={`Preview ${index + 1}`} className="w-full h-full object-cover" />
                   <button type="button" onClick={() => removeImage(index)} className="absolute top-2 right-2 h-6 w-6 rounded-full flex items-center justify-center bg-red-600/80 hover:bg-red-500">
                     <X className="h-4 w-4 text-white" />
                   </button>
                 </div>
               ))}
               {selectedImages.length < 3 && (
                 <label className="aspect-[3/4] rounded-lg border-2 border-dashed flex flex-col items-center justify-center cursor-pointer transition-colors hover:border-[var(--md-sys-color-primary)]" style={{borderColor: 'var(--md-sys-color-outline)'}}>
                    <Upload className="h-8 w-8 mb-2" />
                    <span className="text-sm text-center">Добавить фото <br /><span className="text-xs">до 3</span></span>
                    <input type="file" accept="image/*" multiple onChange={handleImageUpload} className="hidden" />
                 </label>
               )}
            </div>
          </div>
          
          <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
             <h3 className="text-lg font-semibold mb-6">Дополнительная информация</h3>
             <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <FormSelect id="language" label="Язык" registerProps={register('language')}>
                  <option value="ru">Русский</option>
                  <option value="en">Английский</option>
                </FormSelect>
                <FormSelect id="exchangeLocation" label="Точка обмена" required error={errors.exchange_location_id} registerProps={register('exchange_location_id', { required: 'Точка обмена обязательна' })}>
                  <option value="">Выберите точку обмена</option>
                  {exchangeLocations.map(loc => <option key={loc.id} value={loc.id}>{loc.title}</option>)}
                </FormSelect>
             </div>
           </div>
          
          <div className="flex justify-end gap-4 pt-6">
            <button type="button" onClick={() => navigate(-1)} className="py-2 px-4 rounded-lg font-semibold" style={{backgroundColor: 'var(--md-sys-color-surface-container-high)'}}>Отмена</button>
            <button type="submit" disabled={isSubmitting} className="py-2 px-4 rounded-lg font-semibold flex items-center gap-2" style={{backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)'}}>
              {isSubmitting ? (
                <>
                  <div className="w-4 h-4 border-2 border-current border-t-transparent rounded-full animate-spin" />
                  <span>Добавление...</span>
                </>
              ) : (
                <>
                  <Book className="h-4 w-4" />
                  <span>Добавить книгу</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}