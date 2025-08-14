import React, { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { ArrowLeft, User, Save } from 'lucide-react';
import { AuthContext } from '../../App';
import { updateUserProfile, getCities } from '../../api/services';

export default function EditProfilePage() {
    const navigate = useNavigate();
    const { currentUser, fetchUserProfile } = useContext(AuthContext);
    const { register, handleSubmit, formState: { errors, isSubmitting }, setValue } = useForm();
    const [cities, setCities] = useState([]);

    useEffect(() => {
        getCities().then(res => setCities(res.data)).catch(console.error);
        if (currentUser) {
            setValue('username', currentUser.username);
            setValue('bio', currentUser.bio);
            setValue('birth_date', currentUser.birth_date);
            setValue('gender', currentUser.gender);
            setValue('city_id', currentUser.city?.id);
        }
    }, [currentUser, setValue]);

    const onSubmit = async (data) => {
        const payload = { ...data, city_id: parseInt(data.city_id, 10) };
        try {
            await updateUserProfile(payload);
            await fetchUserProfile();
            alert("Профиль успешно обновлен!");
            navigate('/profile');
        } catch (err) {
            console.error("Failed to update profile", err);
            alert("Ошибка при обновлении профиля: " + (err.response?.data?.detail || "Попробуйте еще раз"));
        }
    };

    return (
        <div className="p-8 max-w-2xl mx-auto">
            <div className="flex items-center gap-4 mb-6">
                <button onClick={() => navigate(-1)} className="flex items-center gap-2 font-semibold" style={{ color: 'var(--md-sys-color-primary)' }}>
                    <ArrowLeft className="h-5 w-5" /> Назад
                </button>
                <div className="h-6 w-px" style={{backgroundColor: 'var(--md-sys-color-outline-variant)'}}></div>
                <div className="flex items-center gap-2">
                    <User className="h-5 w-5" style={{ color: 'var(--md-sys-color-primary)' }}/>
                    <h1 className="text-xl font-bold">Редактирование профиля</h1>
                </div>
            </div>
            
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6 p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
                <div>
                    <label htmlFor="username" className="block text-sm font-medium mb-1">Имя пользователя *</label>
                    <input id="username" {...register('username', { required: "Имя обязательно" })} className="w-full p-2 rounded-lg border bg-transparent" />
                    {errors.username && <p className="text-sm text-red-400 mt-1">{errors.username.message}</p>}
                </div>
                
                <div>
                    <label htmlFor="bio" className="block text-sm font-medium mb-1">О себе</label>
                    <textarea id="bio" {...register('bio')} rows="4" className="w-full p-2 rounded-lg border bg-transparent" />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                        <label htmlFor="birth_date" className="block text-sm font-medium mb-1">Дата рождения</label>
                        <input id="birth_date" type="date" {...register('birth_date')} className="w-full p-2 rounded-lg border bg-transparent" />
                    </div>
                     <div>
                        <label htmlFor="gender" className="block text-sm font-medium mb-1">Пол</label>
                        <select id="gender" {...register('gender')} className="w-full p-2 rounded-lg border bg-transparent">
                            <option value="unknown">Не указан</option>
                            <option value="male">Мужской</option>
                            <option value="female">Женский</option>
                        </select>
                    </div>
                </div>

                <div>
                    <label htmlFor="city_id" className="block text-sm font-medium mb-1">Город *</label>
                    <select id="city_id" {...register('city_id', { required: "Город обязателен" })} className="w-full p-2 rounded-lg border bg-transparent">
                        <option value="">Выберите город</option>
                        {cities.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                    </select>
                     {errors.city_id && <p className="text-sm text-red-400 mt-1">{errors.city_id.message}</p>}
                </div>

                <div className="flex justify-end gap-4 pt-4">
                    <button type="button" onClick={() => navigate(-1)} className="py-2 px-4 rounded-lg font-semibold">Отмена</button>
                    <button type="submit" disabled={isSubmitting} className="py-2 px-4 rounded-lg font-semibold flex items-center gap-2" style={{backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)'}}>
                        <Save size={16} /> {isSubmitting ? "Сохранение..." : "Сохранить"}
                    </button>
                </div>
            </form>
        </div>
    );
}