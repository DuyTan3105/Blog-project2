import React from 'react';

const Footer = () => {

    return (
        <footer className="bg-gray-800 text-white text-center w-full">
            <div className="mx-auto pt-2">
                <div className='flex justify-center items-center'>
                    <a href="https://www.facebook.com/your-profile" target="_blank">
                        <img src="/facebook.png" alt="Facebook" className='rounded-full size-10'/>
                    </a>
                    <a href="https://github.com/your-username" target="_blank" className='ml-2'>
                        <img src="/github.png" alt="GitHub" className='rounded-full size-10 '/>
                    </a>
                </div>
                <p className="mt-2">© NoCopyRight 2024</p>
            </div>
        </footer>
    );
}

export default Footer;
