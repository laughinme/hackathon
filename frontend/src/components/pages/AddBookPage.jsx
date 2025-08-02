import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { ArrowLeft, Upload, Book, Camera, X } from 'lucide-react';

export default function AddBookPage({ onAddBook }) {
  const navigate = useNavigate();

  const [selectedImages, setSelectedImages] = useState([]);
  const [imagePreviewUrls, setImagePreviewUrls] = useState([]);
  const [tags, setTags] = useState([]);
  const [currentTag, setCurrentTag] = useState('');

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors, isSubmitting }
  } = useForm({
    defaultValues: {
      language: 'Русский',
      condition: 'good',
      tags: [],
      images: []
    }
  });

  const genres = ['Классика', 'Фэнтези', 'Нон-фикшн', 'Научная фантастика', 'Детектив', 'Роман', 'История', 'Психология'];
  const exchangeLocations = ['Центральная библиотека', 'Метро Сокольники', 'Парк Горького', 'МГУ', 'Красная площадь'];

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

  const addTag = () => {
    if (currentTag.trim() && !tags.includes(currentTag.trim()) && tags.length < 5) {
      const newTags = [...tags, currentTag.trim()];
      setTags(newTags);
      setValue('tags', newTags);
      setCurrentTag('');
    }
  };

  const removeTag = (tagToRemove) => {
    const newTags = tags.filter(tag => tag !== tagToRemove);
    setTags(newTags);
    setValue('tags', newTags);
  };

  const handleFormSubmit = (data) => {
    onAddBook(data);
    alert('Книга успешно добавлена!');
    navigate('/profile');
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
                <FormInput id="author" label="Автор" required placeholder="Введите имя автора" error={errors.author} registerProps={register('author', { required: 'Автор обязателен' })} />
                <FormSelect id="genre" label="Жанр" required error={errors.genre} registerProps={register('genre', { required: 'Жанр обязателен' })}>
                    <option value="">Выберите жанр</option>
                    {genres.map(g => <option key={g} value={g}>{g}</option>)}
                </FormSelect>
                <div className="space-y-1">
                   <label className="text-sm font-medium">Состояние книги *</label>
                   <div className="flex gap-6 pt-2">
                     {['new', 'good', 'fair'].map(condition => (
                       <div key={condition} className="flex items-center space-x-2">
                         <input type="radio" id={condition} value={condition} {...register('condition')} className="w-4 h-4" defaultChecked={condition === 'good'} />
                         <label htmlFor={condition}>{condition === 'new' ? 'Новая' : condition === 'good' ? 'Хорошее' : 'Удовлетворительное'}</label>
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
                <FormInput id="publisher" label="Издательство" placeholder="Название издательства" registerProps={register('publisher')} />
                <FormInput id="year" label="Год издания" type="number" placeholder="2023" min="1500" max={new Date().getFullYear()} registerProps={register('year', { valueAsNumber: true })} />
                <FormSelect id="language" label="Язык" registerProps={register('language')}>
                  {['Русский', 'Английский', 'Другой'].map(lang => <option key={lang} value={lang}>{lang}</option>)}
                </FormSelect>
                <FormSelect id="exchangeLocation" label="Точка обмена" required error={errors.exchangeLocation} registerProps={register('exchangeLocation', { required: 'Точка обмена обязательна' })}>
                  <option value="">Выберите точку обмена</option>
                  {exchangeLocations.map(loc => <option key={loc} value={loc}>{loc}</option>)}
                </FormSelect>
             </div>
             <div className="mt-6">
                <label className="text-sm font-medium">Теги (до 5 тегов)</label>
                <div className="flex gap-2 mt-1">
                    <input value={currentTag} onChange={(e) => setCurrentTag(e.target.value)} onKeyPress={(e) => e.key === 'Enter' && (e.preventDefault(), addTag())} placeholder="Добавить тег" className="w-full p-2 rounded-lg border bg-transparent focus:outline-none focus:ring-2 border-[var(--md-sys-color-outline)] focus:border-[var(--md-sys-color-primary)]" style={{'--tw-ring-color': 'var(--md-sys-color-primary)'}} />
                    <button type="button" onClick={addTag} disabled={!currentTag.trim() || tags.length >= 5} className="py-2 px-4 rounded-lg font-semibold whitespace-nowrap" style={{backgroundColor: 'var(--md-sys-color-secondary-container)', color: 'var(--md-sys-color-on-secondary-container)'}}>Добавить</button>
                </div>
                <div className="flex flex-wrap gap-2 mt-2">
                    {tags.map(tag => (
                        <span key={tag} className="flex items-center gap-1 text-sm px-2 py-1 rounded" style={{backgroundColor: 'var(--md-sys-color-surface-container-high)'}}>
                            {tag}
                            <button type="button" onClick={() => removeTag(tag)} className="ml-1 hover:text-red-400">&times;</button>
                        </span>
                    ))}
                </div>
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