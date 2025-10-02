import { definePreset } from '@primeuix/themes';
import Aura from '@primeuix/themes/aura';

export const MyPreset = definePreset(Aura, {
    semantic: {
        primary: {
            50: '{amber.50}',
            100: '{amber.100}',
            200: '{amber.200}',
            300: '{amber.300}',
            400: '{amber.400}',
            500: '{amber.500}',
            600: '{amber.600}',
            700: '{amber.700}',
            800: '{amber.800}',
            900: '{amber.900}',
            950: '{amber.950}'
        },
        colorScheme: {
            light: {
                surface: {
                    0: '#ffffff',
                    100: '#f8f9fa',
                    200: '#e5e7eb', 
                    700: '#374151', 
                    800: '#1f2937',
                    900: '#111827', 
                    950: '#030712' 
                }
            }
        }
    },
    surface: {
        0: 'rgba(255, 255, 255, 0.8)',
        50: 'rgba(255, 255, 255, 0.85)',
        100: 'rgba(255, 255, 255, 0.9)', 
        700: '#374151',
        800: '#1f2937',
        900: '#111827',
        950: '#030712'
    },
    primitive: {
        font: {
            family: "'Roboto', sans-serif"
        }
    },
    components: {
        inputtext: {
            background: '#ffffff'
        }
    }
});