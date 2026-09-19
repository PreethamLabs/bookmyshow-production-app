import { useEffect, type ReactNode } from 'react';
import { X } from 'lucide-react';
import './Modal.css';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title?: string;
  size?: 'default' | 'lg' | 'xl';
  children: ReactNode;
  footer?: ReactNode;
}

export function Modal({ isOpen, onClose, title, size = 'default', children, footer }: ModalProps) {
  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
    return () => {
      document.body.style.overflow = '';
    };
  }, [isOpen]);

  useEffect(() => {
    const handler = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    if (isOpen) window.addEventListener('keydown', handler);
    return () => window.removeEventListener('keydown', handler);
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  return (
    <div className="cv-modal-overlay" onClick={onClose} role="dialog" aria-modal="true">
      <div
        className={`cv-modal ${size !== 'default' ? `cv-modal--${size}` : ''}`}
        onClick={e => e.stopPropagation()}
      >
        {title && (
          <div className="cv-modal-header">
            <h2 className="cv-modal-title">{title}</h2>
            <button className="cv-modal-close" onClick={onClose} aria-label="Close">
              <X size={18} />
            </button>
          </div>
        )}
        <div className="cv-modal-body">{children}</div>
        {footer && <div className="cv-modal-footer">{footer}</div>}
      </div>
    </div>
  );
}
