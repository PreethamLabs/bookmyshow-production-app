import { type InputHTMLAttributes, type SelectHTMLAttributes, type ReactNode, forwardRef } from 'react';
import './Input.css';

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
  icon?: ReactNode;
}

export const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, icon, className = '', ...props }, ref) => {
    return (
      <div className="cv-input-group">
        {label && <label className="cv-input-label">{label}</label>}
        <div className="cv-input-wrapper">
          {icon && <span className="cv-input-icon">{icon}</span>}
          <input
            ref={ref}
            className={`cv-input ${icon ? 'cv-input--with-icon' : ''} ${error ? 'cv-input--error' : ''} ${className}`}
            {...props}
          />
        </div>
        {error && <span className="cv-input-error">{error}</span>}
      </div>
    );
  }
);
Input.displayName = 'Input';

interface SelectProps extends SelectHTMLAttributes<HTMLSelectElement> {
  label?: string;
  error?: string;
  options: Array<{ value: string | number; label: string }>;
  placeholder?: string;
}

export function Select({ label, error, options, placeholder, className = '', ...props }: SelectProps) {
  return (
    <div className="cv-input-group">
      {label && <label className="cv-input-label">{label}</label>}
      <select className={`cv-select ${className}`} {...props}>
        {placeholder && <option value="">{placeholder}</option>}
        {options.map(opt => (
          <option key={opt.value} value={opt.value}>
            {opt.label}
          </option>
        ))}
      </select>
      {error && <span className="cv-input-error">{error}</span>}
    </div>
  );
}
