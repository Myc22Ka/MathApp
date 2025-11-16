'use client';

import { Input } from '@/components/ui/input';
import { User } from 'lucide-react';
import TextInput from '@/components/inputs/auth/TextInput';
import { IoCalendarNumberOutline, IoPersonSharp } from 'react-icons/io5';
import { AiOutlinePhone } from 'react-icons/ai';
import { FaRegAddressCard, FaTransgender } from 'react-icons/fa';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { UseFormReturn } from 'react-hook-form';
import { ProfileFormData } from '@/lib/zod';
import { Gender } from '@/types/User';
import DashboardSection from './dashboard-section';

interface PersonalDataSectionProps {
    form: UseFormReturn<ProfileFormData>;
}

export function PersonalDataSection({ form }: PersonalDataSectionProps) {
    return (
        <DashboardSection icon={User} title="Dane użytkownika">
            <div className="space-y-5 w-full">
                <div className="grid grid-cols-2 gap-5">
                    <TextInput error={form.formState.errors.firstname?.message} id="firstname" label="Imię" icon={IoPersonSharp}>
                        <Input id="firstname" placeholder="Jan" {...form.register('firstname')} className="pl-10" />
                    </TextInput>

                    <TextInput error={form.formState.errors.lastname?.message} id="lastname" label="Nazwisko" icon={IoPersonSharp}>
                        <Input id="lastname" placeholder="Kowalski" {...form.register('lastname')} className="pl-10" />
                    </TextInput>
                </div>

                <TextInput error={form.formState.errors.phoneNumber?.message} id="phoneNumber" label="Numer telefonu" icon={AiOutlinePhone}>
                    <Input id="phoneNumber" placeholder="+48 123 456 789" {...form.register('phoneNumber')} className="pl-10" />
                </TextInput>

                <TextInput error={form.formState.errors.address?.message} id="address" label="Adres" icon={FaRegAddressCard}>
                    <Input id="address" placeholder="ul. Przykładowa 1, 00-000 Warszawa" {...form.register('address')} className="pl-10" />
                </TextInput>

                <div className="grid grid-cols-2 gap-5">
                    <TextInput error={form.formState.errors.dateOfBirth?.message} id="dateOfBirth" label="Data urodzenia" icon={IoCalendarNumberOutline}>
                        <Input id="dateOfBirth" type="date" {...form.register('dateOfBirth')} className="pl-10" />
                    </TextInput>

                    <TextInput error={form.formState.errors.gender?.message} id="gender" label="Płeć" icon={FaTransgender}>
                        <Select
                            value={form.watch('gender') || 'OTHER'}
                            onValueChange={value =>
                                form.setValue('gender', value as Gender, {
                                    shouldDirty: true,
                                    shouldValidate: true,
                                })
                            }
                        >
                            <SelectTrigger className="w-full pl-10">
                                <SelectValue placeholder="Wybierz płeć" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="MALE">Mężczyzna</SelectItem>
                                <SelectItem value="FEMALE">Kobieta</SelectItem>
                                <SelectItem value="OTHER">Inne</SelectItem>
                            </SelectContent>
                        </Select>
                    </TextInput>
                </div>
            </div>
        </DashboardSection>
    );
}
