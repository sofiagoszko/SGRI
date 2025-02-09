import React, { useState, useEffect } from 'react';
function Countdown({ targetMinutes }) {
    console.log(targetMinutes);
    const calculateTimeLeft = () => {
        const targetTime = new Date().getTime() + targetMinutes * 60 * 1000;
        const difference = targetTime - new Date().getTime();
        if (difference > 0) {
            const timeLeft = {
                hours: Math.floor((difference / (1000 * 60 * 60)) % 24).toString().padStart(2, '0'),
                minutes: Math.floor((difference / 1000 / 60) % 60).toString().padStart(2, '0'),
                seconds: Math.floor((difference / 1000) % 60).toString().padStart(2, '0')
            };
            return timeLeft;
        } else {
            return { hours: '00', minutes: '00', seconds: '00' };
        }
    };
    const [timeLeft, setTimeLeft] = useState(calculateTimeLeft());
    useEffect(() => {
        const timer = setInterval(() => {
            setTimeLeft(calculateTimeLeft());
        }, 1000);
        return () => clearInterval(timer);
    }, [targetMinutes]);
    return (
        <>
            <p>{timeLeft.hours}:{timeLeft.minutes}:{timeLeft.seconds}</p>
        </>
    );
}
export default Countdown;